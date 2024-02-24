package DataAccess;

import server.WebSocket.ResponseException;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface {

    private HashMap<String, UserData> users;

    public UserDAO() { this.users = new HashMap<>(); }

    @Override
    public UserData getUser(String username) { return users.get(username); }

    @Override
    public void createUser(String username, String password, String email) throws ResponseException {
        if (users.containsKey(username)) throw new ResponseException(403, "Error: already taken");
        UserData userData = new UserData(username, password, email);
        users.put(username, userData);
    }

    @Override
    public void createUser(UserData user) throws ResponseException {
        if (users.containsKey(user.username())) throw new ResponseException(403, "Error: already taken");
        users.put(user.username(), user);
    }

    @Override
    public void clearUsers() { users = new HashMap<>(); }
}
