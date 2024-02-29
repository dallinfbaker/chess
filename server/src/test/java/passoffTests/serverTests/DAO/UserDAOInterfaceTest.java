package passoffTests.serverTests.DAO;

import dataAccess.DataAccessException;
import dataAccess.UserDAOInterface;
import dataAccess.databaseDAO.UserDAODB;
import dataAccess.memoryDAO.UserDAOMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOInterfaceTest {

    boolean database = true;
    private UserDAOInterface dao;

    @BeforeEach
    public void setUp() {
        dao = database ? new UserDAODB() : new UserDAOMemory();

        try { dao.clearUsers(); } catch(DataAccessException ignored) {}

        try {
            dao.createUser("myUser", "myPassword", "myEmail");
            dao.createUser("whiteUser", "whitePassword", "whiteEmail");
            dao.createUser("blackUser", "blackPassword", "blackEmail");
        } catch(DataAccessException ignored) {}
    }

    @Test
    void getUserPos() {
        assertDoesNotThrow(() -> dao.getUser("myUser"));
        assertDoesNotThrow(() -> dao.getUser("whiteUser"));
        assertDoesNotThrow(() -> dao.getUser("blackUser"));
    }

    @Test
    void getUserNeg() {
        assertThrows(DataAccessException.class, () -> dao.getUser("user"));
        assertThrows(DataAccessException.class, () -> dao.getUser("p"));
        assertThrows(DataAccessException.class, () -> dao.getUser("theoneandonly"));
    }

    @Test
    void createUserPos() {
        assertDoesNotThrow(() -> dao.createUser("user", "password", "email"));
        assertDoesNotThrow(() -> dao.createUser("k", "o", "d"));
        assertDoesNotThrow(() -> dao.createUser("i", "password", "email"));
    }

    @Test
    void createUserNeg() {
        assertThrows(DataAccessException.class, () -> dao.createUser("myUser", "myPassword", "myEmail"));
        assertThrows(DataAccessException.class, () -> dao.createUser("whiteUser", "", "myEmail"));
        assertThrows(DataAccessException.class, () -> dao.createUser("assertThrows(DataAccessException.class, () -> dao.createUser(whiteUser, , myEmail));", "", "myEmail"));
    }

    @Test
    void clearUsers() {
        assertDoesNotThrow(() -> dao.clearUsers());
        assertThrows(DataAccessException.class, () -> dao.getUser("myUser"));
    }
}