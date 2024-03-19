package ui;

import exception.ResponseException;
import model.AuthDataRecord;
import model.GameDataRecord;
import model.GameListRecord;
import model.UserDataRecord;
import ui.webSocket.NotificationHandler;

import java.net.http.WebSocket;
import java.util.*;

public class Client {
    private String userName = null;
    private String authToken;
    private ServerFacade server = null;
    private final String serverURL;
    private final String port;
    private final NotificationHandler notificationHandler;
    private WebSocket ws;
    private State state = State.SIGNEDOUT;

    public Client (String URL, NotificationHandler nh, String port) {
        serverURL = URL;
        this.port = port;
        notificationHandler = nh;
    }

    public Collection<String> getInput() {
        ArrayList<String> output = new ArrayList<>();
        System.out.printf("%n>>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        output.add(line.toLowerCase().split(" ")[0]);
        output.add(eval(line));
        return output;
    }

    public void preLogin() {
//        System.out.print(SET_BG_COLOR_BLACK);
        while (true) {
            Iterator<String> inputs = getInput().iterator();
            String input = inputs.next();
            String output = inputs.next();
            System.out.printf("%s%n", output);
            if (Objects.equals(output, "quit")) {
                System.out.print("exit program");
                break;
            }
            if (Objects.equals(input, "login") || Objects.equals(input, "register")) { postLogin(); }
        }
    }

    public void postLogin() {
        while (true) {
            Iterator<String> inputs = getInput().iterator();
            String input = inputs.next();
            String output = inputs.next();
            System.out.printf("%s%n", output);
            if (Objects.equals(input, "logout")) {
                System.out.print(output);
                break;
            }
            if (Objects.equals(input, "observe") || Objects.equals(input, "join")) { gamePlay(); }
        }
    }

    public void gamePlay() {

    }

    private void connect() { if (Objects.isNull(server)) server = new ServerFacade(serverURL, port); }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case SIGNEDIN -> postLoginEval(cmd, params);
                case SIGNEDOUT -> preLoginEval(cmd, params);
            };
        } catch (ResponseException e) { return e.getMessage(); }
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
            case "logout" -> logout();
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> joinObserver(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String login(String... params) throws ResponseException {
        connect();
        try {
            userName = params[0];
            UserDataRecord user = new UserDataRecord(userName, params[1], "");
            authToken = server.login(user).authToken();
            state = State.SIGNEDIN;
            return "Logged in";
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String register(String... params) throws ResponseException {
        connect();
        try {
            userName = params[0];
            UserDataRecord user = new UserDataRecord(userName, params[1], params[2]);
            authToken = server.registerUser(user).authToken();
            return "Registered";
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String logout() throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            server.logout(auth);
            state = State.SIGNEDOUT;
            return "Logged out";
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String createGame(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            GameDataRecord game = new GameDataRecord(0, null, null, params[0], null);
            int gameID = server.createGame(auth, game);
            return String.format("Created game %d", gameID);
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String listGames() throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            GameListRecord games = server.listGames(auth);
            return games.toString();
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String joinGame(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            int gameID = server.joinGame(auth, new JoinGameData(Integer.parseInt(params[0]), params[1]));
            return String.format("Joined game: %d", gameID);
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String joinObserver(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            int gameID = server.joinGame(auth, new JoinGameData(Integer.parseInt(params[0]), ""));
            return String.format("Observing game: %d", gameID);
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }

    public String help(){
        if (state == State.SIGNEDOUT) return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
        return """
            create <NAME> - a game
            list - games
            join <ID> [WHITE|BLACK|<empty>] - a game
            observe <ID> - a game
            logout - when you are done
            quit - playing chess
            help - with possible commands
            """;
    }
}
