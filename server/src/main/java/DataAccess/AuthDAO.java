package DataAccess;

import java.util.HashMap;

public class AuthDAO implements AuthDAOInterface {

    private HashMap<String, AuthData> auths;

    public AuthDAO() {
        auths = new HashMap<>();
    }

    @Override
    public AuthData createAuthToken(String username) {
        String token = "";
        AuthData authData = new AuthData(token, username);
        auths.put(token, authData);

        return authData;
    }

    @Override
    public AuthData getAuth(String token) {
        return auths.get(token);
    }

    @Override
    public void deleteAuth(String token) {
        auths.remove(token);

    }

    @Override
    public void clearAuth() {
        auths = new HashMap<>();
    }
}
