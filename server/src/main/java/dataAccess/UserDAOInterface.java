package dataAccess;

import model.UserDataRecord;

public interface UserDAOInterface {
    UserDataRecord getUser(String username) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}
