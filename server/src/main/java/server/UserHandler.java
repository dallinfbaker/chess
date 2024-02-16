package server;

import DataAccess.AuthData;
import DataAccess.DAOManager;
import DataAccess.UserData;
import server.WebSocket.ResponseException;
import service.UserService;

public class UserHandler {
    private final UserService userService;
    public UserHandler(DAOManager daoManager) {
        userService = new UserService(daoManager);
    }

    public AuthData register(UserData user) throws ResponseException {
        return userService.register(user);
    }
    public AuthData login(UserData user) throws ResponseException {
        return userService.login(user);
    }
    public void logout(String token) throws ResponseException {
        userService.logout(token);
    }
}
