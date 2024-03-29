package service;

import dataAccess.*;
import model.AuthDataRecord;
import model.UserDataRecord;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import exception.ResponseException;
import java.util.Objects;

public class UserService {
    private final DAOManager daoManager;

    public UserService(DAOManager daoManager) { this.daoManager = daoManager; }

    public AuthDataRecord register(UserDataRecord user) throws ResponseException {
        if (
            Objects.isNull(user.username()) ||
            Objects.isNull(user.email()) ||
            Objects.isNull(user.password()) ||
            Objects.equals(user.username(), "") ||
            Objects.equals(user.password(), "") ||
            Objects.equals(user.email(), "")
        ) throw new ResponseException(400, "Error: bad request");
        try {
            daoManager.userDAO.createUser(user.username(), user.password(), user.email());
            return daoManager.authDAO.createAuthToken(user.username());
        } catch (DataAccessException e) { throw new ResponseException(403, "Error: already exists"); }
    }

    public AuthDataRecord login(UserDataRecord user) throws ResponseException {
        try {
            UserDataRecord other = daoManager.userDAO.getUser(user.username());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (other == null) throw new ResponseException(401, "Error: unauthorized");
            else if (!encoder.matches(user.password(), other.password())) throw new ResponseException(401, "Error: unauthorized");
            else return daoManager.authDAO.createAuthToken(user.username());
        } catch (DataAccessException e) { throw new ResponseException(401, "Error: unauthorized"); }
    }

    public void logout(String token) throws ResponseException {
        try { daoManager.authDAO.deleteAuth(token); }
        catch (DataAccessException e) { throw new ResponseException(401, "Error: unauthorized"); }
    }
}
