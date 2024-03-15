package serviceTests;

import dataAccess.DAOManager;
import dataAccess.DataAccessException;
import model.UserDataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.ResponseException;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    boolean database = true;

    private UserService service;

    @BeforeEach
    public void setUp() {
        DAOManager dao = new DAOManager(database);
        try {
            dao.userDAO.clearUsers();
            dao.authDAO.clearAuth();
        } catch (DataAccessException ignored) {}
        service = new UserService(dao);

        try {
            dao.userDAO.createUser("myUser", "myPassword", "myEmail");
            dao.userDAO.createUser("whiteUser", "whitePassword", "whiteEmail");
            dao.userDAO.createUser("blackUser", "blackPassword", "blackEmail");
        } catch(DataAccessException ignored) {}
    }
    @Test
    void registerPos() {
        assertDoesNotThrow(() -> service.register(new UserDataRecord("user", "password", "email")));
        assertDoesNotThrow(() -> service.register(new UserDataRecord("k", "o", "d")));
        assertDoesNotThrow(() -> service.register(new UserDataRecord("i", "password", "email")));
    }

    @Test
    void registerNeg() {
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord("myUser", "myPassword", "myEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord("whiteUser", "whitePassword", "whiteEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord("blackUser", "blackPassword", "blackEmail")));
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord("", "", "")));
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord("", null, "a")));
        assertThrows(ResponseException.class, () -> service.register(new UserDataRecord(null, null, null)));
    }

    @Test
    void loginPos() {
        assertDoesNotThrow(() -> service.login(new UserDataRecord("myUser", "myPassword", "myEmail")));
        assertDoesNotThrow(() -> service.login(new UserDataRecord("whiteUser", "whitePassword", "whiteEmail")));
        assertDoesNotThrow(() -> service.login(new UserDataRecord("blackUser", "blackPassword", "blackEmail")));
    }

    @Test
    void loginNeg() {
        assertThrows(ResponseException.class, () -> service.login(new UserDataRecord("user", "password", "email")));
        assertThrows(ResponseException.class, () -> service.login(new UserDataRecord("", "", "")));
        assertThrows(ResponseException.class, () -> service.login(new UserDataRecord(null, null, null)));
    }

    @Test
    void logoutPos() {
        assertDoesNotThrow(() -> service.logout(service.login(new UserDataRecord("myUser", "myPassword", "myEmail")).authToken()));
        assertDoesNotThrow(() -> service.logout(service.login(new UserDataRecord("whiteUser", "whitePassword", "whiteEmail")).authToken()));
        assertDoesNotThrow(() -> service.logout(service.login(new UserDataRecord("blackUser", "blackPassword", "blackEmail")).authToken()));
    }

    @Test
    void logoutNeg() {
        assertDoesNotThrow(() -> service.logout("this is a token"));
        assertDoesNotThrow(() -> service.logout("this is also a token"));
        assertDoesNotThrow(() -> service.logout(null));
    }
}