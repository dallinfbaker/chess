package DataAccess;

import model.AuthDataRecord;
import server.WebSocket.ResponseException;

public interface AuthDAOInterface {

    AuthDataRecord createAuthToken(String username) throws DataAccessException;
    AuthDataRecord getAuth(String token) throws DataAccessException;
    boolean validAuth(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clearAuth() throws DataAccessException;

}
