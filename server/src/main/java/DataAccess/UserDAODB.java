package DataAccess;

import model.UserDataRecord;
import server.WebSocket.ResponseException;

public class UserDAODB implements UserDAOInterface {

    UserDAODB() {
        try {
        DatabaseManager.getConnection();
        } catch (DataAccessException ignored) {}
    }

    @Override
    public UserDataRecord getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public void createUser(UserDataRecord user) throws DataAccessException {

    }

    @Override
    public void clearUsers() throws DataAccessException {

    }
}
