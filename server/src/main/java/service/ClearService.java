package service;

import dataAccess.DAOManager;
import dataAccess.DataAccessException;

public class ClearService {
    private final DAOManager daoManager;
    public ClearService(DAOManager daoManager) { this.daoManager = daoManager; }

    public void clear() {
        try {
            daoManager.authDAO.clearAuth();
            daoManager.gameDAO.clearGames();
            daoManager.userDAO.clearUsers();
        } catch (DataAccessException ignored) {}
    }
}
