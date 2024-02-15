package server;

import DataAccess.DAOManager;

public class ClearHandler {
    private final DAOManager daoManager;
    public ClearHandler(DAOManager daoManager){
        this.daoManager = daoManager;
    }

    public void handle() {
        daoManager.authDAO.clearAuth();
        daoManager.gameDAO.clearGames();
        daoManager.userDAO.clearUsers();
    }
}
