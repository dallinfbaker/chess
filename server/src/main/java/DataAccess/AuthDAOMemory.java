package DataAccess;

import model.AuthDataRecord;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAOMemory implements AuthDAOInterface {

    private HashMap<String, AuthDataRecord> auths;

    public AuthDAOMemory() { auths = new HashMap<>(); }

    @Override
    public AuthDataRecord createAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        AuthDataRecord authData = new AuthDataRecord(token, username);
        auths.put(token, authData);

        return authData;
    }

    @Override
    public AuthDataRecord getAuth(String token) { return auths.get(token); }
    @Override
    public boolean validAuth(String auth) { return auths.containsKey(auth); }

    @Override
    public void deleteAuth(String token) { auths.remove(token); }

    @Override
    public void clearAuth() { auths = new HashMap<>(); }

    public void addAuth(String token, String user) {
        AuthDataRecord authData = new AuthDataRecord(token, user);
        auths.put(token, authData);
    }
}
