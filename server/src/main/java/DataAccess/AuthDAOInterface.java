package DataAccess;

import model.AuthDataRecord;

public interface AuthDAOInterface {

    AuthDataRecord createAuthToken(String username) throws DataAccessException;
    AuthDataRecord getAuth(String token) throws DataAccessException;
    boolean validAuth(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clearAuth() throws DataAccessException;

}
