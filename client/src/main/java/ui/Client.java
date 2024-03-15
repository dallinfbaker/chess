package ui;

import exception.ResponseException;
import ui.webSocket.NotificationHandler;

import java.net.http.WebSocket;
import java.util.Arrays;

public class Client {
    private String userName = null;
    private final ServerFacade server;
    private final String serverURL;
    private final NotificationHandler notificationHandler;
    private WebSocket ws;
    private State state = State.SIGNEDOUT;

    public Client (String URL, NotificationHandler nh, String port) {
        serverURL = URL;
        server = new ServerFacade(serverURL, port);
        notificationHandler = nh;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case SIGNEDIN -> postLoginEval(cmd, params);
                case SIGNEDOUT -> preLoginEval(cmd, params);
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    private String preLoginEval(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "login" -> login(params);
            case "register" -> register(params);
            case "quit" -> "quit";
            default -> help();
        };
    }
    private String postLoginEval(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "logout" -> login(params);
            case "create" -> createGame(params);
            case "list" -> listGames(params);
            case "join" -> joinGame(params);
            case "observe" -> joinObserver(params);
            default -> help();
        };
    }

    public String login(String... params) throws ResponseException {
        return "";
    }
    public String register(String... params) throws ResponseException {
        return "";
    }
    public String logout(String... params) throws ResponseException {
        return "";
    }
    public String createGame(String... params) throws ResponseException {
        return "";
    }
    public String listGames(String... params) throws ResponseException {
        return "";
    }
    public String joinGame(String... params) throws ResponseException {
        return "";
    }
    public String joinObserver(String... params) throws ResponseException {
        return "";
    }

    public String help() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
        }
        return """
            create <NAME> - a game
            list - games
            join <ID> [WHITE|BLACK|<empty>] - a game
            observe <ID> - a game
            quit - playing chess
            help - with possible commands
            """;
    }
}
