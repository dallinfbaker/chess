package service;

import DataAccess.DAOManager;
import server.WebSocket.ResponseException;

public class ClearService {
    private final DAOManager daoManager;
    public ClearService(DAOManager daoManager) { this.daoManager = daoManager; }

    public void clear() throws ResponseException {
        daoManager.authDAO.clearAuth();
        daoManager.gameDAO.clearGames();
        daoManager.userDAO.clearUsers();
    }
}
