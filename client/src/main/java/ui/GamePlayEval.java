package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.DrawChessBoard;
import model.GameDataRecord;
import ui.webSocket.NotificationHandler;
import ui.webSocket.WebSocketFacade;

import java.util.Iterator;
import java.util.Objects;

public class GamePlayEval extends EvalLoop {
    private final GameDataRecord game;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    protected GamePlayEval(ServerFacade serverFacade, String serverURL, String port, GameDataRecord gameDataRecord) {
        super(serverFacade, serverURL, port);
        game = gameDataRecord;
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
//        try {
//            ws = new WebSocketFacade(serverURL + ":" + port, notificationHandler);
//        } catch (Exception ignored) {}

        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
        } while (!Objects.equals(input, "leave") && !Objects.equals(output, "quit"));
        return output;
    }

    private String redrawBoard() { return DrawChessBoard.drawBoard(game.game().getBoard(), true); }
    private String resign() { return null; }

    private String showLegalMoves(String... params) {
        return DrawChessBoard.drawBoard(game.game().getBoard(), true);
    }

    public String makeMove(String... params) throws ResponseException {
        return "lol, you really thought you could move? amateur";
    }
    public String leaveGame(String... params) throws ResponseException {
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
}
