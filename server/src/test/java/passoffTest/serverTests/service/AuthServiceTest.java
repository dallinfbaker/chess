package passoffTest.serverTests.service;

import DataAccess.AuthDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocket.ResponseException;
import service.AuthService;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService service;

    @BeforeEach
    public void setUp() {
        AuthDAO dao = new AuthDAO();
        service = new AuthService(dao);

        dao.addAuth("1234","myUser");
        dao.addAuth("White","whiteUser");
        dao.addAuth("Black","blackUser");
    }

    @Test
    void checkAuthPos() {
        assertDoesNotThrow(() -> service.checkAuth("Black"));
        assertDoesNotThrow(() -> service.checkAuth("White"));
        assertDoesNotThrow(() -> service.checkAuth("1234"));
    }

    @Test
    void checkAuthNeg() {
        assertThrows(ResponseException.class, () -> service.checkAuth("black"));
        assertThrows(ResponseException.class, () -> service.checkAuth("white"));
        assertThrows(ResponseException.class, () -> service.checkAuth("4321"));
    }

    @Test
    void getUsernamePos() {
        assertDoesNotThrow(() -> service.getUsername("Black"));
        assertDoesNotThrow(() -> service.getUsername("White"));
        assertDoesNotThrow(() -> service.getUsername("1234"));
    }

    @Test
    void getUsernameNeg() {
        assertThrows(NullPointerException.class, () -> service.getUsername("black"));
        assertThrows(NullPointerException.class, () -> service.getUsername("white"));
        assertThrows(NullPointerException.class, () -> service.getUsername("4321"));
    }
}