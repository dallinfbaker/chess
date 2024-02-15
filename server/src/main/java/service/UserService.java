package service;
import DataAccess.*;

public class UserService {

    private final AuthDAO authDAO;
//    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    public UserService(AuthDAO auth, UserDAO user) {
        authDAO = auth;
//        gameDAO = game;
        userDAO = user;
    }

    public AuthData register(UserData user) {
        userDAO.createUser(user);
        return authDAO.createAuthToken(user.getUsername());
    }
    public AuthData login(UserData user) {
        return authDAO.createAuthToken(user.getUsername());
    }
    public void logout(String token) {
        authDAO.deleteAuth(token);
    }
}
