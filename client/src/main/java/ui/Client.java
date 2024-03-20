package ui;

import exception.ResponseException;
import model.*;
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
    private State state = State.signedOut;
    private Map<Integer, GameDataRecord> gameList = new HashMap<>();

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
        state = State.signedOut;
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
            if (Objects.equals(input, "login") || Objects.equals(input, "register")) {
                output = postLogin();
                state = State.signedOut;
            }
            if (Objects.equals(output, "quit")) { break; }
        } while (!Objects.equals(output, "quit"));
        System.out.print("exit program");
    }
    public String postLogin() {
        state = State.signedIn;
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
            if ((Objects.equals(input, "observe") || Objects.equals(input, "join")) && Objects.equals(output.split(" ")[1], "game:")) {
                output = gamePlay();
                state = State.signedIn;
            }
        } while (!Objects.equals(input, "logout") && !Objects.equals(output, "quit"));
        return output;
    }
    public String gamePlay() {
        state = State.playing;
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
        } while (!Objects.equals(input, "leave") && !Objects.equals(output, "quit"));
        return output;
    }

    private void connect() { if (Objects.isNull(server)) server = new ServerFacade(serverURL, port); }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case signedIn -> postLoginEval(cmd, params);
                case signedOut -> preLoginEval(cmd, params);
                case playing -> gamePlayEval(cmd, params);
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
            case "observe" -> joinGame(params);
            case "quit" -> "quit";
            default -> help();
        };
    }
    private String gamePlayEval(String cmd, String... params) throws ResponseException {
        return switch (cmd) {
            case "move" -> makeMove(params);
            case "leave" -> leaveGame(params);
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
            return "Logged out";
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String createGame(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            GameDataRecord game = new GameDataRecord(0, null, null, params[0], null);
            server.createGame(auth, game);
            gameList.put(gameList.size() + 1, game);
            return String.format("Created game %s", params[0]);
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String listGames() throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            GameListRecord games = server.listGames(auth);
            StringBuilder output = new StringBuilder();
            output.append("games:\n\n");
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
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String joinGame(String... params) throws ResponseException {
        try {
            AuthDataRecord auth = new AuthDataRecord(authToken, userName);
            int gameId = gameList.get(Integer.parseInt(params[0])).gameID();
            StringBuilder output = new StringBuilder();
            try {
                server.joinGame(auth, new JoinGameData(gameId, params[1]));
                output.append("Joined");
            } catch (IndexOutOfBoundsException e) {
                server.joinGame(auth, new JoinGameData(gameId, null));
                output.append("Observing");
            }
            output.append(" game: ").append(gameList.get(Integer.parseInt(params[0])).gameName()).append("\n");
            output.append(DrawChessBoard.drawBoard(gameList.get(Integer.parseInt(params[0])).game().getBoard(), false));
            output.append(DrawChessBoard.drawBoard(gameList.get(Integer.parseInt(params[0])).game().getBoard(), true));
            return output.toString();
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String makeMove(String... params) throws ResponseException {
        return "lol, you really thought you could move? amateur";
    }
    public String leaveGame(String... params) throws ResponseException {
        return "Left game";
    }

    public String help() {
        return switch (state) {
            case signedIn -> """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
            case signedOut -> """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
            case playing -> """
                move <START POSITION> <END POSITION> - to move a piece
                leave - to leave game
                quit - playing chess
                help - with possible commands
                """;
        };
    }
}
