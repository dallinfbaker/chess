package DataAccess;

import server.WebSocket.ResponseException;

import java.util.HashMap;
import java.util.Objects;

public class UserDAO implements UserDAOInterface {

    private HashMap<String, UserData> users;

    public UserDAO() {
        this.users = new HashMap<>();
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        return users.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) throws ResponseException {
        if (users.containsKey(username)) throw new ResponseException(403, "Error: already taken");
        UserData userData = new UserData(username, password, email);
        users.put(username, userData);
    }

    @Override
    public void createUser(UserData user) throws ResponseException {
        if (users.containsKey(user.getUsername())) throw new ResponseException(403, "Error: already taken");
        users.put(user.getUsername(), user);
    }

    @Override
    public void updateUser(String username, String password, String email) throws ResponseException {
        UserData userData = new UserData(username, password, email);
        users.replace(username, userData);
    }

    @Override
    public void deleteUser(String username) throws ResponseException {
        users.remove(username);
    }

    @Override
    public void clearUsers() throws ResponseException {
        users = new HashMap<>();
    }
}
