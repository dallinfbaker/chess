package DataAccess;

import model.AuthDataRecord;

public class AuthDAODB  implements AuthDAOInterface {
    @Override
    public AuthDataRecord createAuthToken(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthDataRecord getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public boolean validAuth(String token) throws DataAccessException {
        return false;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clearAuth() throws DataAccessException {

    }
}
