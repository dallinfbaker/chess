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

    public Client (String URL, NotificationHandler nh) {
        serverURL = URL;
        server = new ServerFacade(serverURL, "1666");
        notificationHandler = nh;
    }

    public String eval(String input) {
        switch (state) {
            case SIGNEDIN -> { return postLoginEval(input); }
            case SIGNEDOUT -> { return preLoginEval(input); }
            default -> { return ""; }
        }
    }
    private String preLoginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

    }
    private String postLoginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> login(params);
                case "creategame" -> createGame(params);
                case "listgames" -> listGames(params);
                case "joingame" -> joinGame(params);
                case "joinobserver" -> joinObserver(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
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
        return "";
    }
}
