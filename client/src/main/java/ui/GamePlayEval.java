package ui;

import chess.ChessMove;
import chess.ChessPosition;
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
import java.util.Iterator;
import java.util.Objects;

import static model.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static model.EscapeSequences.SET_TEXT_COLOR_RED;

public class GamePlayEval extends EvalLoop implements ServerMessageHandler {
    private WebSocketFacade webSocket;
    private GameDataRecord game;
    private final AuthDataRecord auth;
    private Collection<ChessMove> moves;
    protected GamePlayEval(ServerFacade serverFacade, String serverURL, String port, GameDataRecord gameDataRecord) {
        super(serverFacade, serverURL, port);
        game = gameDataRecord;
        auth = new AuthDataRecord(authToken, userName);
    }

    @Override
    public String eval(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "move" -> makeMove(params);
                case "leave" -> leaveGame(params);
                case "resign" -> resign();
                case "show" -> showLegalMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) { return e.getMessage(); }
    }

    @Override
    public String loop() {
        try {
            webSocket = new WebSocketFacade(serverUrl + ":" + port, this);
        } catch (Exception ignored) { return "unable to connect"; }
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

    private String redrawBoard() { return DrawChessBoard.drawBoard(game.game().getBoard(), true); }
    private String resign() throws ResponseException {
        webSocket.resignGame(game, auth);
        return "you lost the game";
    }
    private String showLegalMoves(String... params) {
        moves = game.game().validMoves(new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
        return DrawChessBoard.drawBoard(game.game().getBoard(), true);
    }
    public String makeMove(String... params) throws ResponseException {
        ChessMove move = (ChessMove) moves.toArray()[Integer.parseInt(params[0])];
        webSocket.makeMove(game, auth, move);
        return redrawBoard();
    }
    public String leaveGame(String... params) throws ResponseException {
        webSocket.leaveGame(game, auth);
        return "Left game";
    }
    @Override
    public String help() {
        return """
            move <START POSITION> <END POSITION> - to move a piece
            leave - to leave game
            quit - playing chess
            help - with possible commands""";
    }

    @Override
    public void notify(NotificationMessage message) {
        System.out.printf("%s%s\n", SET_TEXT_COLOR_GREEN, message.getMessage());
    }

    @Override
    public void loadGame(LoadGameMessage message) {
        game = message.getGameData();
        redrawBoard();
    }

    @Override
    public void errorHandler(ErrorMessage message) {
        System.out.printf("%s%s\n", SET_TEXT_COLOR_RED, message.getMessage());
    }
}
