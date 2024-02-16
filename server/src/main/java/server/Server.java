package server;

import DataAccess.AuthData;
import DataAccess.DAOManager;
import DataAccess.GameData;
import DataAccess.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import server.WebSocket.Connection;
import server.WebSocket.WebSocketHandler;
import server.WebSocket.ResponseException;
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
        clearHandler.handle();
        res.status(200);
        return "";
    }
    public Object registerUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userHandler.register(user);
//        res.status(200);
        return new Gson().toJson(auth);
    }
    public Object login(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userHandler.login(user);
//        res.status(200);
        return new Gson().toJson(auth);
    }
    public Object logout(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        userHandler.logout(req.headers("authorization"));
//        res.status(200);
        return "";
    }
    public Object listGames(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        HashMap<Integer, GameData> games = gameHandler.listGames();
//        res.status(200);
        return new Gson().toJson(games);
    }
    public Object createGame(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        Map<String, Integer> id = gameHandler.createGame(new Gson().fromJson(req.body(), GameData.class).getGameName());
//        res.status(200);
        return new Gson().toJson(id);
    }
    public Object joinGame(Request req, Response res) throws ResponseException {
        authHandler(req.headers("authorization"));
        gameHandler.joinGame();
//        res.status(200);
        return "";
    }

}
