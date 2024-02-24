package DataAccess;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAO implements AuthDAOInterface {

    private HashMap<String, AuthData> auths;

    public AuthDAO() { auths = new HashMap<>(); }

    @Override
    public AuthData createAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        auths.put(token, authData);

        return authData;
    }

    @Override
    public AuthData getAuth(String token) { return auths.get(token); }
    @Override
    public boolean validAuth(String auth) { return auths.containsKey(auth); }

    @Override
    public void deleteAuth(String token) { auths.remove(token); }

    @Override
    public void clearAuth() { auths = new HashMap<>(); }

    public void addAuth(String token, String user) {
        AuthData authData = new AuthData(token, user);
        auths.put(token, authData);
    }
}
