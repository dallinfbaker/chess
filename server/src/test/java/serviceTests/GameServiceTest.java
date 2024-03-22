package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.databaseDAO.GameDAODB;
import dataAccess.GameDAOInterface;
import dataAccess.memoryDAO.GameDAOMemory;
import chess.ChessGame;
import model.GameDataRecord;
import model.GameListRecord;
import model.ObservingUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.ResponseException;
import service.GameService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    boolean database = true;
    private GameService service;

    @BeforeEach
    public void setUp() {
        GameDAOInterface dao;
        if (database) {
            dao = new GameDAODB();
        }
        else { dao = new GameDAOMemory(); }
        try { dao.clearGames(); }
        catch (DataAccessException ignored) {}

        service = new GameService(dao);

        GameDataRecord data = new GameDataRecord(1234, null, null, "", new ChessGame(), new ObservingUsers(new HashSet<>()));
        dao.addGame(data);
        data = new GameDataRecord(4321, null, null, "", new ChessGame(), new ObservingUsers(new HashSet<>()));
        dao.addGame(data);
        data = new GameDataRecord(2341, null, null, "", new ChessGame(), new ObservingUsers(new HashSet<>()));
        dao.addGame(data);
        data = new GameDataRecord(3412, null, null, "", new ChessGame(), new ObservingUsers(new HashSet<>()));
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
    void createGameNeg() { assertThrows(ResponseException.class, () -> service.createGame(null)); }

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
    void listGamesPos() throws ResponseException {
        assertDoesNotThrow(() -> service.listGames());
        assertSame(GameListRecord.class, service.listGames().getClass());
        assertEquals(4, service.listGames().games().size());
        assertDoesNotThrow(() -> service.listGames().games());
        System.out.print(service.listGames().toString());
    }

    @Test
    void listGamesNeg() throws ResponseException {
        service = new GameService(new GameDAOMemory());

        assertEquals(0, service.listGames().games().size());
    }
}