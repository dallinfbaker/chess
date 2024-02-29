package passoffTests.serverTests.DAO;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.databaseDAO.GameDAODB;
import dataAccess.memoryDAO.GameDAOMemory;
import model.GameDataRecord;
import model.GameListRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOInterfaceTest {

    boolean database = true;
    private GameDAOInterface dao;

    @BeforeEach
    public void setUp() {
        dao = database ? new GameDAODB() : new GameDAOMemory();

        try { dao.clearGames(); } catch(DataAccessException ignored) {}

        dao.addGame(new GameDataRecord(1234, "white", "black", "normal", new ChessGame()));
        dao.addGame(new GameDataRecord(2345, "", "", "noUsername", new ChessGame()));
        dao.addGame(new GameDataRecord(3456, null, null, "game", new ChessGame()));
    }
    @Test
    void getGamesPos() throws DataAccessException {
        assertDoesNotThrow(() -> dao.getGames());
        assertEquals(3, dao.getGames().games().size());
        assertEquals(GameListRecord.class, dao.getGames().getClass());
    }

    @Test
    void getGamesNeg() throws DataAccessException {
        dao.clearGames();
        assertDoesNotThrow(() -> dao.getGames());
        assertEquals(0, dao.getGames().games().size());
    }

    @Test
    void getGamePos() throws DataAccessException {
        assertDoesNotThrow(() -> dao.getGame(1234));
        assertDoesNotThrow(() -> dao.getGame(2345));
        assertDoesNotThrow(() -> dao.getGame(3456));
        assertEquals("normal", dao.getGame(1234).gameName());
        assertEquals("white", dao.getGame(1234).whiteUsername());
    }

    @Test
    void getGameNeg() {
        assertThrows(DataAccessException.class, () -> dao.getGame(1111));
        assertThrows(DataAccessException.class, () -> dao.getGame(22222222));
        assertThrows(DataAccessException.class, () -> dao.getGame(333333333));
    }

    @Test
    void createGameDataPos() {
        assertDoesNotThrow(() -> dao.createGameData("1234"));
        assertDoesNotThrow(() -> dao.createGameData("2345"));
        assertDoesNotThrow(() -> dao.createGameData("3456"));
    }

    @Test
    void createGameDataNeg() {
        assertThrows(DataAccessException.class, () -> dao.createGameData("dhsghtoperhgonjsgbnsrlkgbslkrgjbdlksbgkjlsdbgrlkbsrgklj"));
        assertThrows(DataAccessException.class, () -> dao.createGameData(null));
        assertDoesNotThrow(() -> dao.createGameData("normal"));
    }

    @Test
    void setWhiteUsernamePos() {
        assertDoesNotThrow(() -> dao.setWhiteUsername(1234, "white"));
        assertDoesNotThrow(() -> dao.setWhiteUsername(3456, "white"));
    }

    @Test
    void setWhiteUsernameNeg() {
        assertThrows(DataAccessException.class, () -> dao.setWhiteUsername(3456, "sjdhjfjkfjhfdjhyfkjfkhgfjhfjhgfjhgfhjgfjhfggdcjhchvkjhvjkhkjhfkjfgkfkgfghh"));
    }

    @Test
    void setBlackUsernamePos() {
        assertDoesNotThrow(() -> dao.setBlackUsername(1234, "black"));
        assertDoesNotThrow(() -> dao.setBlackUsername(3456, "black"));
        assertDoesNotThrow(() -> dao.setBlackUsername(3456, ""));
        assertDoesNotThrow(() -> dao.setBlackUsername(3456, "bldsgrgsgack"));
    }

    @Test
    void setBlackUsernameNeg() {
        assertThrows(DataAccessException.class, () -> dao.setBlackUsername(3456, "sjdhjfjkfjhfdjhyfkjfkhgfjhfjhgfjhgfhjgfjhfggdcjhchvkjhvjkhkjhfkjfgkfkgfghh"));
    }

    @Test
    void addGamePos() {
        assertDoesNotThrow(() -> dao.addGame(new GameDataRecord(11234, "white", "black", "normal", new ChessGame())));
        assertDoesNotThrow(() -> dao.addGame(new GameDataRecord(12234, "white", "black", "normal", new ChessGame())));
        assertDoesNotThrow(() -> dao.addGame(new GameDataRecord(13234, "white", "black", "normal", new ChessGame())));
    }

    @Test
    void addGameNeg() throws DataAccessException {
        assertDoesNotThrow(() -> dao.addGame(new GameDataRecord(1234, "whitee", "bbbbblack", "nooooooormal", null)));
        assertDoesNotThrow(() -> dao.addGame(new GameDataRecord(1234, "whitebbb", "blaaaaaack", "noaaarmal", new ChessGame())));
        assertEquals("normal", dao.getGame(1234).gameName());
        assertEquals("white", dao.getGame(1234).whiteUsername());
    }

    @Test
    void clearGames() {
        assertDoesNotThrow(() -> dao.clearGames());
        assertThrows(DataAccessException.class, () -> dao.getGame(1233333));
    }
}