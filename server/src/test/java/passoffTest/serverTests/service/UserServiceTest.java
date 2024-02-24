package passoffTest.serverTests.service;

import DataAccess.DAOManager;
import DataAccess.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocket.ResponseException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    public void setUp() {
        DAOManager dao = new DAOManager();
        service = new UserService(dao);

        try {
            dao.userDAO.createUser("myUser", "myPassword", "myEmail");
            dao.userDAO.createUser("whiteUser", "whitePassword", "whiteEmail");
            dao.userDAO.createUser("blackUser", "blackPassword", "blackEmail");
        } catch(ResponseException ignored) {}
    }
    @Test
    void registerPos() {
        assertDoesNotThrow(() -> service.register(new UserData("user", "password", "email")));
    }
    @Test
    void registerNeg() {
        assertThrows(ResponseException.class, () -> service.register(new UserData("myUser", "myPassword", "myEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("whiteUser", "whitePassword", "whiteEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("blackUser", "blackPassword", "blackEmail")));
    }

    @Test
    void loginPos() {
        assertDoesNotThrow(() -> service.login(new UserData("myUser", "myPassword", "myEmail")));
        assertDoesNotThrow(() -> service.login(new UserData("whiteUser", "whitePassword", "whiteEmail")));
        assertDoesNotThrow(() -> service.login(new UserData("blackUser", "blackPassword", "blackEmail")));
    }
    @Test
    void loginNeg() {
        assertThrows(ResponseException.class, () -> service.login(new UserData("user", "password", "email")));
    }

    @Test
    void logoutPos() {
        assertDoesNotThrow(() -> service.logout(service.login(new UserData("myUser", "myPassword", "myEmail")).authToken()));
        assertDoesNotThrow(() -> service.logout(service.login(new UserData("whiteUser", "whitePassword", "whiteEmail")).authToken()));
        assertDoesNotThrow(() -> service.logout(service.login(new UserData("blackUser", "blackPassword", "blackEmail")).authToken()));
    }
    @Test
    void logoutNeg() {
        assertDoesNotThrow(() -> service.logout("this is a token"));
    }
}