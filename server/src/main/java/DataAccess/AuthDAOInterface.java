package DataAccess;

import model.AuthDataRecord;
import server.WebSocket.ResponseException;

public interface AuthDAOInterface {

    AuthDataRecord createAuthToken(String username) throws ResponseException;
    AuthDataRecord getAuth(String token) throws ResponseException;
    boolean validAuth(String token) throws ResponseException;
    void deleteAuth(String token) throws ResponseException;
    void clearAuth() throws ResponseException;

}
