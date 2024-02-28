package DataAccess;

import model.UserDataRecord;
import server.WebSocket.ResponseException;

import java.util.HashMap;

public class UserDAOMemory implements UserDAOInterface {

    private HashMap<String, UserDataRecord> users;

    public UserDAOMemory() { this.users = new HashMap<>(); }

    @Override
    public UserDataRecord getUser(String username) { return users.get(username); }

    @Override
    public void createUser(String username, String password, String email) throws ResponseException {
        if (users.containsKey(username)) throw new ResponseException(403, "Error: already taken");
        UserDataRecord userData = new UserDataRecord(username, password, email);
        users.put(username, userData);
    }

    @Override
    public void createUser(UserDataRecord user) throws ResponseException {
        if (users.containsKey(user.username())) throw new ResponseException(403, "Error: already taken");
        users.put(user.username(), user);
    }

    @Override
    public void clearUsers() { users = new HashMap<>(); }
}
