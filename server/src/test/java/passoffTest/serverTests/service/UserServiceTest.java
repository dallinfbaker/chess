package passoffTest.serverTests.service;

import DataAccess.DAOManager;
import model.UserData;
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
        assertDoesNotThrow(() -> service.register(new UserData("k", "o", "d")));
        assertDoesNotThrow(() -> service.register(new UserData("i", "password", "email")));
    }

    @Test
    void registerNeg() {
        assertThrows(ResponseException.class, () -> service.register(new UserData("myUser", "myPassword", "myEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("whiteUser", "whitePassword", "whiteEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("blackUser", "blackPassword", "blackEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("", "", "")));
        assertThrows(ResponseException.class, () -> service.register(new UserData("", null, "a")));
        assertThrows(ResponseException.class, () -> service.register(new UserData(null, null, null)));
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
        assertThrows(ResponseException.class, () -> service.login(new UserData("", "", "")));
        assertThrows(ResponseException.class, () -> service.login(new UserData(null, null, null)));
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
        assertDoesNotThrow(() -> service.logout("this is also a token"));
        assertDoesNotThrow(() -> service.logout(null));
    }
}