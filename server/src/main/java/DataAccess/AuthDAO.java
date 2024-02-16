package DataAccess;

import server.WebSocket.ResponseException;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAO implements AuthDAOInterface {

    private HashMap<String, AuthData> auths;

    public AuthDAO() {
        auths = new HashMap<>();
    }

    @Override
    public AuthData createAuthToken(String username) throws ResponseException {
//        String token = UUID.nameUUIDFromBytes(username.getBytes()).toString();
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        auths.put(token, authData);

        return authData;
    }

    @Override
    public AuthData getAuth(String token) throws ResponseException {
        return auths.get(token);
    }
    @Override
    public boolean validAuth(String auth) {
        return auths.containsKey(auth);
    }

    @Override
    public void deleteAuth(String token) throws ResponseException {
        auths.remove(token);
    }

    @Override
    public void clearAuth() throws ResponseException {
        auths = new HashMap<>();
    }
}
