package server;

import DataAccess.DAOManager;
import spark.*;

public class Server {

    private DAOManager daoManager = new DAOManager();
    private ClearHandler clearHandler = new ClearHandler(daoManager);
    private GameHandler gameHandler = new GameHandler(daoManager);
    private UserHandler userHandler = new UserHandler(daoManager);
    private LogoutHandler logoutHandler = new LogoutHandler(daoManager);
    private RegisterHandler registerHandler = new RegisterHandler(daoManager);

    public Server(){}
    public Server(DAOManager daoManager) {
        this.daoManager = daoManager;
        clearHandler = new ClearHandler(daoManager);
        gameHandler = new GameHandler(daoManager);
        userHandler = new UserHandler(daoManager);
        logoutHandler = new LogoutHandler(daoManager);
        registerHandler = new RegisterHandler(daoManager);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void clearData(){
        clearHandler.handle();
    }
    public void registerUser(){
        userHandler.register();
    }
    public void login(){
        userHandler.login();
    }
    public void logout(){
        userHandler.logout();
    }
    public void listGames(){
        gameHandler.listGames();
    }
    public void createGame(){
        gameHandler.createGame();
    }
    public void joinGame(){
        gameHandler.joinGame();
    }

}
