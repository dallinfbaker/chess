package ui;

import exception.ResponseException;
import model.AuthDataRecord;
import model.GameDataRecord;
import model.GameListRecord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class PostLoginEval extends EvalLoop {
    private int gameId;
    private Map<Integer, GameDataRecord> gameList = new HashMap<>();
    private boolean reverse = true;
    private GameDataRecord game;

    protected PostLoginEval(ServerFacade serverFacade, String serverURL, String port) { super(serverFacade, serverURL, port); }

    @Override
    public String eval(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) { return e.getMessage(); }
    }

    @Override
    public String loop() {
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
            if ((Objects.equals(input, "observe") || Objects.equals(input, "join")) && Objects.equals(output.split(" ")[1], "game:")) {
                GamePlayEval gamePlayEval = new GamePlayEval(server, serverUrl, port, game, reverse);
                output = gamePlayEval.loop();
            }
        } while (!Objects.equals(input, "logout") && !Objects.equals(output, "quit"));
        return output;
    }

    public String logout() throws ResponseException {
        server.logout(new AuthDataRecord(authToken, userName));
        return "Logged out";
    }
    public String createGame(String... params) throws ResponseException {
        gameList.put(gameList.size() + 1, server.createGame(new AuthDataRecord(authToken, userName), params));
        return String.format("Created game %s", gameList.get(gameList.size()).gameName());
    }
    public String listGames() throws ResponseException {
        GameListRecord games = server.listGames(new AuthDataRecord(authToken, userName));
        StringBuilder output = new StringBuilder("games:\n\n");
        gameList = new HashMap<>();
        int i = 1;
        for (GameDataRecord data : games.games()) {
            output.append("game number: ").append(i).append("\n");
            output.append("game name: ").append(data.gameName()).append("\n");
            output.append("white player: ").append(data.whiteUsername()).append("\n");
            output.append("black player: ").append(data.blackUsername()).append("\n\n");
            gameList.put(i++, data);
        }
        return output.toString();
    }
    public String joinGame(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            game = gameList.get(Integer.parseInt(params[0]));
            try {
                if (Objects.equals("black", params[1].toLowerCase())) {
                    reverse = false;
                    game = new GameDataRecord(game.gameID(), game.whiteUsername(), userName, game.gameName(), game.game(), game.observers());
                }
                else if (Objects.equals("white", params[1].toLowerCase()))
                { game = new GameDataRecord(game.gameID(), userName, game.blackUsername(), game.gameName(), game.game(), game.observers()); }
            }
            catch (IndexOutOfBoundsException ignored) {}
            return server.joinGame(auth, game.gameID(), params) + " game: " + game.gameName() + "\n";
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }

    @Override
    public String help() {
        return """
            create <NAME> - a game
            list - games
            join <ID> [WHITE|BLACK|<empty>] - a game
            observe <ID> - a game
            logout - when you are done
            quit - playing chess
            help - with possible commands""";
    }
}
