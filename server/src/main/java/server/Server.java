package server;

import DataAccess.*;
import com.google.gson.*;
import server.WebSocket.*;
import service.GameService;
import spark.*;

import java.util.*;

public class Server {

    private DAOManager daoManager = new DAOManager();
    private ClearHandler clearHandler = new ClearHandler(daoManager);
    private GameHandler gameHandler = new GameHandler(daoManager.gameDAO);
    private UserHandler userHandler = new UserHandler(daoManager);
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();


    public Server() {}
//    public Server(DAOManager daoManager) {
//        this.daoManager = daoManager;
//        clearHandler = new ClearHandler(daoManager);
//        gameHandler = new GameHandler(daoManager);
//        userHandler = new UserHandler(daoManager);
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        Spark.delete("/db", this::clearData);

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);

        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

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
        res.body(new Gson().toJson(new ExceptionHandler(str)));
    }

    private void authHandler(String auth) throws ResponseException {
        if (!daoManager.authDAO.validAuth(auth)) throw new ResponseException(401, "Error: unauthorized");
    }

    private Object clearData(Request req, Response res) throws ResponseException {
        try {
            clearHandler.handle();
            return "";
        } catch (ResponseException e) { throw e; }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object registerUser(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userHandler.register(user);
            return new Gson().toJson(auth);
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object login(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userHandler.login(user);
            res.status(200);
            return new Gson().toJson(auth);
        } catch (ResponseException e) { throw e; }
        catch (JsonSyntaxException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object logout(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            userHandler.logout(req.headers("authorization"));
            return "";
        } catch (ResponseException e) { throw e; }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object listGames(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            HashMap<Integer, GameData> games = gameHandler.listGames();
            GameList list = new GameList(games);
            return new Gson().toJson(list);
        } catch (ResponseException e) { throw e; }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object createGame(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            Map<String, Integer> id = gameHandler.createGame(new Gson().fromJson(req.body(), GameData.class).getGameName());
            return new Gson().toJson(id);
        } catch (ResponseException e) { throw e; }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
    public Object joinGame(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        try {
            GameData data = gameHandler.joinGame(req.body(), daoManager.authDAO.getAuth(req.headers("authorization")).getUsername());
            if (Objects.isNull(data)) return "";
            else return "";
        } catch (ResponseException e) { throw e; }
        catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
}
