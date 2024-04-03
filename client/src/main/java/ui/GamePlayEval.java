package ui;

import chess.*;
import exception.ResponseException;
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
    private Collection<ChessMove> moves;
    private final boolean reversed;
    private final boolean player;
    private final ChessGame.TeamColor color;
    protected GamePlayEval(ServerFacade serverFacade, String serverURL, String port, GameDataRecord gameDataRecord, boolean reverse) {
        super(serverFacade, serverURL, port);
        game = gameDataRecord;
        reversed = reverse;
        moves = new HashSet<>();
        player = Objects.equals(game.whiteUsername(), userName) || Objects.equals(game.blackUsername(), userName);
        ChessGame.TeamColor color1 = reversed ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        if (!player) color1 = null;
        color = color1;
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
        try { webSocket.joinGame(game, authToken, userName); }
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
        if (!player) return "you are not a player";
        webSocket.resignGame(game.gameID(), authToken);
        return "you lost the game";
    }
    private String showLegalMoves(String... params) {
        moves = game.game().validMoves(getChessPosition(params[0]));
        return redrawBoard();
    }
    public String makeMove(String... params) throws ResponseException {
        if (!player) return "you are not a player";
        ChessPosition start = getChessPosition(params[0]), end = getChessPosition(params[1]);
        ChessPiece.PieceType promotionType = null;
        if (params.length > 2) promotionType = switch (params[2].toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
        ChessMove move = new ChessMove(start, end, promotionType);
        if (!Objects.equals(game.game().getTeamTurn(), color)) return "illegal move: it isn't your turn";
        if (!Objects.equals(game.game().getBoard().getPiece(start).getTeamColor(), color)) return "illegal move: that isn't your piece";
        if (!isLegalMove(move)) return "illegal move: you can't move there";
        webSocket.makeMove(game, authToken, move);
        try { game.game().makeMove(move); } catch (InvalidMoveException ignored) {}
        moves = new HashSet<>();
        return redrawBoard();
    }
    private ChessPosition getChessPosition(String input) { return new ChessPosition(Integer.parseInt(input.substring(1)), input.toLowerCase().charAt(0) - 'a' + 1); }
    private boolean isLegalMove(ChessMove move) {
        try { return game.game().validMoves(move.getStartPosition()).contains(move); }
        catch (NullPointerException e) { return false; }
    }

    public String leaveGame() throws ResponseException {
        webSocket.leaveGame(game, authToken);
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
        game = message.getGame();
//        System.out.printf("\n%s%s%s\n", SET_TEXT_COLOR_BLUE, message.getMessage(), SET_TEXT_COLOR_WHITE);
        System.out.printf(redrawBoard());
        System.out.printf("%n>>> ");
    }

    @Override
    public void errorHandler(ErrorMessage message) {
        System.out.printf("\n%s%s%s\n", SET_TEXT_COLOR_RED, message.getErrorMessage(), SET_TEXT_COLOR_WHITE);
        System.out.printf("\n%n>>> ");
    }
}
