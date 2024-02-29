package dataAccess;

import model.UserDataRecord;

import java.util.HashMap;

public class UserDAOMemory implements UserDAOInterface {

    private HashMap<String, UserDataRecord> users;

    public UserDAOMemory() { this.users = new HashMap<>(); }

    @Override
    public UserDataRecord getUser(String username) { return users.get(username); }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        if (users.containsKey(username)) throw new DataAccessException("Error: already taken");
        UserDataRecord userData = new UserDataRecord(username, password, email);
        users.put(username, userData);
    }

    @Override
    public void createUser(UserDataRecord user) throws DataAccessException {
        if (users.containsKey(user.username())) throw new DataAccessException("Error: already taken");
        users.put(user.username(), user);
    }

    @Override
    public void clearUsers() { users = new HashMap<>(); }
}
