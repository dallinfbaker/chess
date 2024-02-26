package passoffTest.serverTests.service;

import DataAccess.DAOManager;
import DataAccess.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocket.ResponseException;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private ClearService service;

    @BeforeEach
    public void setUp() {
        DAOManager dao = new DAOManager();
        service = new ClearService(dao);

        GameData data = new GameData(1234);
        dao.gameDAO.addGame(data);
        data = new GameData(4321);
        dao.gameDAO.addGame(data);
        data = new GameData(2341);
        dao.gameDAO.addGame(data);
        data = new GameData(3412);
        data.setWhiteUsername("dd");
        dao.gameDAO.addGame(data);

        dao.authDAO.addAuth("1234","myUser");
        dao.authDAO.addAuth("White","whiteUser");
        dao.authDAO.addAuth("Black","blackUser");

        try {
            dao.userDAO.createUser("myUser", "myPassword", "myEmail");
            dao.userDAO.createUser("whiteUser", "whitePassword", "whiteEmail");
            dao.userDAO.createUser("blackUser", "blackPassword", "blackEmail");
        } catch(ResponseException ignored) {}
    }
    @Test
    void clearPos() { assertDoesNotThrow(() -> service.clear()); }
}