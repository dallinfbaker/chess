package service;

import DataAccess.DAOManager;

public class ClearService {
    private final DAOManager daoManager;
    public ClearService(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public void Clear(){
        daoManager.authDAO.clearAuth();
        daoManager.gameDAO.clearGames();
        daoManager.userDAO.clearUsers();
    }
}
