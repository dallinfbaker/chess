package server;

import DataAccess.*;
import com.google.gson.*;
import server.WebSocket.*;
import service.AuthService;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.*;

public class Server {

    private final DAOManager daoManager = new DAOManager();
    private final AuthService authService = new AuthService(daoManager.authDAO);
    private final ClearService clearService = new ClearService(daoManager);
    private final UserService userService = new UserService(daoManager);
    private final GameService gameService = new GameService(daoManager.gameDAO);
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();


    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        Spark.delete("/db", this::clearHandler);

        Spark.post("/user", this::registerUserHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);

        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        String str = "message: " + ex.getMessage();
        res.body(new Gson().toJson(new ExceptionRecord(str)));
    }
    private void authHandler(String auth) throws ResponseException { authService.checkAuth(auth); }
    private Object clearHandler(Request req, Response res) throws ResponseException {
        try {
            clearService.clear();
            return "";
        } catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object registerUserHandler(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            if (
                Objects.isNull(user.getUsername()) ||
                Objects.isNull(user.getEmail()) ||
                Objects.isNull(user.getPassword())
            ) throw new ResponseException(400, "Error: bad request");
            return new Gson().toJson(userService.register(user));
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object loginHandler(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            return new Gson().toJson(userService.login(user));
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object logoutHandler(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            userService.logout(req.headers("authorization"));
            return "";
        } catch (ResponseException e) { throw e; }
        catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object listGamesHandler(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            HashMap<Integer, GameData> games = gameService.listGames();
            return new Gson().toJson(new GameList(games));
        } catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object createGameHandler(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            Map<String, Integer> id = gameService.createGame(new Gson().fromJson(req.body(), GameData.class).getGameName());
            return new Gson().toJson(id);
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
    public Object joinGameHandler(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            String username = authService.getUsername(req.headers("authorization"));
            JsonObject jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
            try {
                int gameID = jsonObject.get("gameID").getAsInt();
                try {
                    String playerColor = jsonObject.get("playerColor").getAsString();
                    gameService.joinGame(gameID, playerColor, username);
                } catch (NullPointerException e) { gameService.watchGame(gameID); }
            } catch (NullPointerException e) { throw new ResponseException(400, "Error: bad request"); }
            return "";
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }
}
