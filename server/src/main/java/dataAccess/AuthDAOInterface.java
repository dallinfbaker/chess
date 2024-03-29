package dataAccess;

import model.AuthDataRecord;

public interface AuthDAOInterface {

    AuthDataRecord createAuthToken(String username) throws DataAccessException;
    AuthDataRecord getAuth(String token) throws DataAccessException;
    boolean validAuth(String token);
    void deleteAuth(String token) throws DataAccessException;
    void clearAuth() throws DataAccessException;
    void addAuth(String number, String myUser);
}
