package server;

import DataAccess.DAOManager;
import server.WebSocket.ResponseException;

public class ClearHandler {
    private final DAOManager daoManager;
    public ClearHandler(DAOManager daoManager){
        this.daoManager = daoManager;
    }

    public void handle() throws ResponseException {
        daoManager.authDAO.clearAuth();
        daoManager.gameDAO.clearGames();
        daoManager.userDAO.clearUsers();
    }
}
