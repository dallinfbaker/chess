package DataAccess;

import model.UserData;
import server.WebSocket.ResponseException;

public interface UserDAOInterface {
    UserData getUser(String username) throws ResponseException;
    void createUser(String username, String password, String email) throws ResponseException;
    void createUser(UserData user) throws ResponseException;
//    void updateUser(String username, String password, String email) throws ResponseException;
//    void deleteUser(String username) throws ResponseException;
    void clearUsers() throws ResponseException;
}
