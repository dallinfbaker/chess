package service;

import DataAccess.GameDAO;
import DataAccess.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.WebSocket.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

//    private GameDAO dao;
    private GameService service;

    @BeforeEach
    public void setUp() {
        GameDAO dao = new GameDAO();
        service = new GameService(dao);

        GameData data = new GameData(1234);
        dao.addGame(data);
        data = new GameData(4321);
        dao.addGame(data);
        data = new GameData(2341);
        dao.addGame(data);
        data = new GameData(3412);
        data.setWhiteUsername("dd");
        dao.addGame(data);

    }

    @Test
    void createGame() {
        service.createGame("myGame");

//        Assertions.assertEquals(1, 4);
    }

    @Test
    void joinGame() throws ResponseException {
//        setUp();

        service.joinGame(3412, "WHITE", "dd");
    }

    @Test
    void watchGame() throws ResponseException {

        try {
            service.watchGame(1);
        } catch (Exception e) {
            Assertions.assertTrue(e.getClass() == ResponseException.class);
        }
    }

    @Test
    void listGames() {
//        setUp();

        service.listGames();
    }
}