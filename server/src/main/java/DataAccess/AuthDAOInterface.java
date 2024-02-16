package DataAccess;

import server.WebSocket.ResponseException;

public interface AuthDAOInterface {

    AuthData createAuthToken(String username) throws ResponseException;
    AuthData getAuth(String token) throws ResponseException;
    boolean validAuth(String token) throws ResponseException;
    void deleteAuth(String token) throws ResponseException;
    void clearAuth() throws ResponseException;

}
