package service;

import DataAccess.*;
import server.WebSocket.ResponseException;
import java.util.Objects;

public class UserService {
    private final DAOManager daoManager;

    public UserService(DAOManager daoManager) { this.daoManager = daoManager; }

    public AuthData register(UserData user) throws ResponseException {
        if (
            Objects.isNull(user.username()) ||
            Objects.isNull(user.email()) ||
            Objects.isNull(user.password()) ||
            Objects.equals(user.username(), "") ||
            Objects.equals(user.password(), "") ||
            Objects.equals(user.email(), "")
        ) throw new ResponseException(400, "Error: bad request");
        daoManager.userDAO.createUser(user);
        return daoManager.authDAO.createAuthToken(user.username());
    }

    public AuthData login(UserData user) throws ResponseException {
        UserData other = daoManager.userDAO.getUser(user.username());
        if (other == null) throw new ResponseException(401, "Error: unauthorized");
        else if (!Objects.equals(other.password(), user.password())) throw new ResponseException(401, "Error: unauthorized");
        else return daoManager.authDAO.createAuthToken(user.username());
    }

    public void logout(String token) { daoManager.authDAO.deleteAuth(token); }
}
