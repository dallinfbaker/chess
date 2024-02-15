package server;

import DataAccess.DAOManager;
import com.google.gson.Gson;
import server.WebSocket.WebSocketHandler;
import server.WebSocket.ResponseException;
import spark.*;

public class Server {

    private DAOManager daoManager = new DAOManager();
    private ClearHandler clearHandler = new ClearHandler(daoManager);
    private GameHandler gameHandler = new GameHandler(daoManager);
    private UserHandler userHandler = new UserHandler(daoManager);
    private WebSocketHandler webSocketHandler = new WebSocketHandler();


    public Server() {}
    public Server(DAOManager daoManager) {
        this.daoManager = daoManager;
        clearHandler = new ClearHandler(daoManager);
        gameHandler = new GameHandler(daoManager);
        userHandler = new UserHandler(daoManager);
    }

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

//        Spark.delete("/pet/:id", this::deletePet);

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
    }

    private Object clearData(Request req, Response res) throws ResponseException{
        clearHandler.handle();
        res.status(200);
        return "";
    }
    public Object registerUser(Request req, Response res) throws ResponseException {
        userHandler.register();
        res.status(200);
        return "";
    }
    public Object login(Request req, Response res) throws ResponseException {
        userHandler.login();
        res.status(200);
        return "";
    }
    public Object logout(Request req, Response res) throws ResponseException {
        userHandler.logout();
        res.status(200);
        return "";
    }
    public Object listGames(Request req, Response res) throws ResponseException {
        gameHandler.listGames();
        res.status(200);
        return "";
    }
    public Object createGame(Request req, Response res) throws ResponseException {
        gameHandler.createGame();
        res.status(200);
        return "";
    }
    public Object joinGame(Request req, Response res) throws ResponseException {
        gameHandler.joinGame();
        res.status(200);
        return "";
    }

}
