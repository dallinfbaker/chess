package DataAccess;

import model.UserDataRecord;
import server.WebSocket.ResponseException;

public interface UserDAOInterface {
    UserDataRecord getUser(String username) throws ResponseException;
    void createUser(String username, String password, String email) throws ResponseException;
    void createUser(UserDataRecord user) throws ResponseException;
//    void updateUser(String username, String password, String email) throws ResponseException;
//    void deleteUser(String username) throws ResponseException;
    void clearUsers() throws ResponseException;
}
