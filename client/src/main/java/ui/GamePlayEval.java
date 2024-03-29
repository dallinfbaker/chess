package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import exception.ResponseException;
import model.AuthDataRecord;
import model.DrawChessBoard;
import model.GameDataRecord;
import ui.webSocket.ServerMessageHandler;
import ui.webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import static model.EscapeSequences.*;

public class GamePlayEval extends EvalLoop implements ServerMessageHandler {
    private WebSocketFacade webSocket;
    private GameDataRecord game;
    private final AuthDataRecord auth;
    private Collection<ChessMove> moves;
    private final boolean reversed;
    protected GamePlayEval(ServerFacade serverFacade, String serverURL, String port, GameDataRecord gameDataRecord, boolean reverse) {
        super(serverFacade, serverURL, port);
        game = gameDataRecord;
        auth = new AuthDataRecord(authToken, userName);
        reversed = reverse;
        moves = new HashSet<>();
    }

    @Override
    public String eval(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "move" -> makeMove(params);
                case "leave" -> leaveGame();
                case "resign" -> resign();
                case "show" -> showLegalMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) { return e.getMessage(); }
    }

    @Override
    public String loop() {
        try { webSocket = new WebSocketFacade(serverUrl + ":" + port, this); }
        catch (Exception e) {
            System.out.print("unable to connect\n");
            System.out.print(e.getMessage());
            return "unable to connect";
        }
        try { webSocket.joinGame(game, auth); }
        catch (ResponseException e) { System.out.printf("%d %s\n", e.statusCode(), e.getMessage()); }
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s\n", output);
        } while (!Objects.equals(input, "leave") && !Objects.equals(output, "quit"));
        return output;
    }

    private String redrawBoard() { return DrawChessBoard.drawBoard(game.game().getBoard(), reversed, moves); }
    private String resign() throws ResponseException {
        webSocket.resignGame(game.gameID(), auth);
        return "you lost the game";
    }
    private String showLegalMoves(String... params) {
        moves = game.game().validMoves(getChessPosition(params[1]));//new ChessPosition(Integer.parseInt(params[0].substring(1)), letterToInt(params[0].substring(2))));
        return redrawBoard();
    }
    public String makeMove(String... params) throws ResponseException {
        ChessPosition start = getChessPosition(params[0]); // new ChessPosition(Integer.parseInt(params[0].substring(1)), letterToInt(params[0].substring(2)));
        ChessPosition end = getChessPosition(params[1]);// new ChessPosition(Integer.parseInt(params[1].substring(1)), letterToInt(params[1].substring(2)));
        ChessPiece.PieceType promotionType = null;
        if (params.length > 2) promotionType = switch (params[2].toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
        ChessMove move = new ChessMove(start, end, promotionType);
        if (!isLegalMove(move)) return "illegal move";
        webSocket.makeMove(game, auth, move);
        try { game.game().makeMove(move); } catch (InvalidMoveException ignored) {}
        moves = new HashSet<>();
        return redrawBoard();
    }
    private ChessPosition getChessPosition(String input) { return new ChessPosition(Integer.parseInt(input.substring(1)), letterToInt(input.toLowerCase().charAt(0))); }
    private int letterToInt(Character letter) { return letter - 'a' + 1; }
    private boolean isLegalMove(ChessMove move) { return game.game().validMoves(move.getStartPosition()).contains(move); }

    public String leaveGame() throws ResponseException {
        webSocket.leaveGame(game, auth);
        return "Left game";
    }
    @Override
    public String help() {
        return """
            redraw - the board
            move <start letter> <start number> <end letter> <end number> <Piece promotion> [QUEEN|BISHOP|ROOK|KNIGHT|<empty>] - to move a piece
            leave - to leave game
            resign - the game
            show <start letter> <start number> - to show legal moves
            quit - playing chess
            help - with possible commands""";
    }

    @Override
    public void notify(NotificationMessage message) {
        System.out.printf("\n%s%s%s\n", SET_TEXT_COLOR_GREEN, message.getMessage(), SET_TEXT_COLOR_WHITE);
        System.out.printf("%n>>> ");
    }

    @Override
    public void loadGame(LoadGameMessage message) {
        moves = new HashSet<>();
        game = message.getGameData();
//        System.out.printf("\n%s%s%s\n", SET_TEXT_COLOR_BLUE, message.getMessage(), SET_TEXT_COLOR_WHITE);
        System.out.printf(redrawBoard());
        System.out.printf("%n>>> ");
    }

    @Override
    public void errorHandler(ErrorMessage message) {
        System.out.printf("\n%s%s%s\n", SET_TEXT_COLOR_RED, message.getMessage(), SET_TEXT_COLOR_WHITE);
        System.out.printf("\n%n>>> ");
    }
}
