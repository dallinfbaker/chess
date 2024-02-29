package dataAccess;

import model.UserDataRecord;

public interface UserDAOInterface {
    UserDataRecord getUser(String username) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    void createUser(UserDataRecord user) throws DataAccessException;
//    void updateUser(String username, String password, String email) throws ResponseException;
//    void deleteUser(String username) throws ResponseException;
    void clearUsers() throws DataAccessException;
}
