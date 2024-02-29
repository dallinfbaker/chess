package passoffTests.serverTests.DAO;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.databaseDAO.AuthDAODB;
import dataAccess.memoryDAO.AuthDAOMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOInterfaceTest {

    boolean database = true;
    private AuthDAOInterface dao;

    @BeforeEach
    public void setUp() {
        dao = database ? new AuthDAODB() : new AuthDAOMemory();

        try { dao.clearAuth(); } catch(DataAccessException ignored) {}

        dao.addAuth("1234", "myUser");
        dao.addAuth("2345", "whiteUser");
        dao.addAuth("3456", "blackUser");
    }

    @Test
    void createAuthTokenPos() throws DataAccessException {
        assertDoesNotThrow(() -> dao.createAuthToken("myUser"));
        assertDoesNotThrow(() -> dao.createAuthToken("whiteUser"));
        assertDoesNotThrow(() -> dao.createAuthToken("blackUser"));
        assertNotNull(dao.createAuthToken("blackUser"));
    }

    @Test
    void createAuthTokenNeg() {
        assertThrows(DataAccessException.class, () -> dao.createAuthToken("assertThrows(DataAccessException.class, () -> dao.createUser(whiteUser, , myEmail)); , ,myEmail));"));
        assertThrows(DataAccessException.class, () -> dao.createAuthToken(null));
    }

    @Test
    void getAuthPos() throws DataAccessException {
        assertDoesNotThrow(() -> dao.getAuth("1234"));
        assertDoesNotThrow(() -> dao.getAuth("2345"));
        assertDoesNotThrow(() -> dao.getAuth("3456"));
        assertNotNull(dao.getAuth("1234"));
        assertEquals("myUser", dao.getAuth("1234").username());
    }

    @Test
    void getAuthNeg() {
        assertThrows(DataAccessException.class, () -> dao.getAuth(null));
        assertThrows(DataAccessException.class, () -> dao.getAuth("null"));
    }

    @Test
    void validAuthPos() {
        assertDoesNotThrow(() -> dao.validAuth("1234"));
        assertDoesNotThrow(() -> dao.validAuth("2345"));
        assertDoesNotThrow(() -> dao.validAuth("3456"));
        assertTrue(dao.validAuth("1234"));
    }

    @Test
    void validAuthNeg() {
        assertFalse(dao.validAuth("4444444"));
        assertFalse(dao.validAuth("4444444fdihgshsgidsflgjksdfhglsdkjbgflkbfdglkbfdsglbfsdlkjgbdsbfgljfdkbglkbfdbsfgdl"));
    }

    @Test
    void deleteAuthPos() {
        assertDoesNotThrow(() -> dao.deleteAuth("1234"));
        assertDoesNotThrow(() -> dao.deleteAuth("2345"));
        assertDoesNotThrow(() -> dao.deleteAuth("3456"));
    }

    @Test
    void deleteAuthNeg() {
        assertDoesNotThrow(() -> dao.deleteAuth(null));
        assertDoesNotThrow(() -> dao.deleteAuth("null"));
        assertDoesNotThrow(() -> dao.deleteAuth("4444444fdihgshsgidsflgjksdfhglsdkjbgflkbfdglkbfdsglbfsdlkjgbdsbfgljfdkbglkbfdbsfgdl"));
    }

    @Test
    void addAuthPos() {
        assertDoesNotThrow(() -> dao.addAuth("4321", "myUser"));
        assertDoesNotThrow(() -> dao.addAuth("5432", "whiteUser"));
        assertDoesNotThrow(() -> dao.addAuth("6543", "blackUser"));
    }

    @Test
    void addAuthNeg() {
        assertDoesNotThrow(() -> dao.addAuth("1234", "myUser"));
        assertDoesNotThrow(() -> dao.addAuth("2345", "whiteUser"));
        assertDoesNotThrow(() -> dao.addAuth("3456", "blackUser"));
    }

    @Test
    void clearAuth() {
        assertDoesNotThrow(() -> dao.clearAuth());
        assertThrows(DataAccessException.class, () -> dao.getAuth("1234"));
    }
}