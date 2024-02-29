package passoffTests.serverTests.service;

import dataAccess.DAOManager;
import dataAccess.DataAccessException;
import chess.ChessGame;
import model.GameDataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private ClearService service;

    @BeforeEach
    public void setUp() {
        DAOManager dao = new DAOManager(false);
        service = new ClearService(dao);

        GameDataRecord data = new GameDataRecord(1234, "", "", "", new ChessGame());
        dao.gameDAO.addGame(data);
        data = new GameDataRecord(4321, "", "", "", new ChessGame());
        dao.gameDAO.addGame(data);
        data = new GameDataRecord(2341, "", "", "", new ChessGame());
        dao.gameDAO.addGame(data);
        data = new GameDataRecord(3412, "", "", "", new ChessGame());
        try {
            dao.gameDAO.setWhiteUsername(3412, "white");
            dao.gameDAO.setBlackUsername(3412, "black");
        } catch (Exception ignored) {}
        dao.gameDAO.addGame(data);

        dao.authDAO.addAuth("1234","myUser");
        dao.authDAO.addAuth("White","whiteUser");
        dao.authDAO.addAuth("Black","blackUser");

        try {
            dao.userDAO.createUser("myUser", "myPassword", "myEmail");
            dao.userDAO.createUser("whiteUser", "whitePassword", "whiteEmail");
            dao.userDAO.createUser("blackUser", "blackPassword", "blackEmail");
        } catch(DataAccessException ignored) {}
    }
    @Test
    void clearPos() { assertDoesNotThrow(() -> service.clear()); }
}