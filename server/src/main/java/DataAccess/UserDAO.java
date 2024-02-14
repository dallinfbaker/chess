package DataAccess;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface {

    private HashMap<String, UserData> users;

    public UserDAO() {
        this.users = new HashMap<>();
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        users.put(username, userData);
    }

    @Override
    public void updateUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        users.replace(username, userData);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }

    @Override
    public void clearUsers() {
        users = new HashMap<>();
    }

    @Override
    public String getPassword(String username) {
        return users.get(username).getPassword();
    }

    @Override
    public String getEmail(String username) {
        return users.get(username).getEmail();
    }
}
