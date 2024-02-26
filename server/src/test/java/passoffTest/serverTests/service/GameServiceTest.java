package passoffTest.serverTests.service;

import DataAccess.GameDAO;
import chess.ChessGame;
import model.GameDataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocket.ResponseException;
import service.GameService;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService service;

    @BeforeEach
    public void setUp() {
        GameDAO dao = new GameDAO();
        service = new GameService(dao);

        GameDataRecord data = new GameDataRecord(1234, null, null, "", new ChessGame());
        dao.addGame(data);
        data = new GameDataRecord(4321, null, null, "", new ChessGame());
        dao.addGame(data);
        data = new GameDataRecord(2341, null, null, "", new ChessGame());
        dao.addGame(data);
        data = new GameDataRecord(3412, null, null, "", new ChessGame());
        dao.addGame(data);
        try {
            dao.setWhiteUsername(3412, "white");
            dao.setBlackUsername(3412, "black");
        } catch (Exception ignored) {}

    }

    @Test
    void createGamePos() {
        assertDoesNotThrow(() -> service.createGame("myGame"));
        assertDoesNotThrow(() -> service.createGame("anotherGame"));
        assertDoesNotThrow(() -> service.createGame(""));
        assertDoesNotThrow(() -> service.createGame("myGame"));
    }

    @Test
    void createGameNeg() { assertThrows(Exception.class, () -> service.createGame(null)); }

    @Test
    void joinGamePos() {
        assertDoesNotThrow(() -> service.joinGame(1234, "WHITE", "white"));
        assertDoesNotThrow(() -> service.joinGame(4321, "BLACK", "black"));
        assertDoesNotThrow(() -> service.joinGame(2341, "BLACK", "white"));
        assertDoesNotThrow(() -> service.joinGame(3412, "WHITE", "white"));
    }

    @Test
    void joinGameNeg() {
        assertThrows(ResponseException.class, () -> service.joinGame(3412, "WHITE", "black"));
        assertThrows(ResponseException.class, () -> service.joinGame(3412, "BLACK", "white"));
        assertDoesNotThrow(() -> service.joinGame(1234, "WHITE", "white"));
        assertThrows(ResponseException.class, () -> service.joinGame(1234, "WHITE", "black"));
        assertThrows(ResponseException.class, () -> service.joinGame(2, "BLACK", "black"));
    }

    @Test
    void watchGamePos() {
        assertDoesNotThrow(() -> service.watchGame(1234));
        assertDoesNotThrow(() -> service.watchGame(4321));
        assertDoesNotThrow(() -> service.watchGame(2341));
        assertDoesNotThrow(() -> service.watchGame(3412));
    }

    @Test
    void watchGameNeg() {
        assertThrows(ResponseException.class, () -> service.watchGame(1));
        assertThrows(ResponseException.class, () -> service.watchGame(1111111111));
        assertThrows(ResponseException.class, () -> service.watchGame(-4));
    }

    @Test
    void listGamesPos() {
        assertDoesNotThrow(() -> service.listGames());
        assertSame(HashMap.class, service.listGames().getClass());
        assertEquals(4, service.listGames().size());
        assertDoesNotThrow(() -> service.listGames().get(1234));
    }

    @Test
    void listGamesNeg() {
        service = new GameService(new GameDAO());

        assertEquals(0, service.listGames().size());
        assertNull(service.listGames().get(1234));
    }
}