package ui;

import exception.ResponseException;

import java.util.Iterator;
import java.util.Objects;

public class GamePlayEval extends EvalLoop {
    protected GamePlayEval(ServerFacade serverFacade, String serverURL, String port) {
        super(serverFacade, serverURL, port);
    }
//    public GamePlayEval(ServerFacade server) { super(server); }

    @Override
    public String eval(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "move" -> makeMove(params);
                case "leave" -> leaveGame(params);
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
