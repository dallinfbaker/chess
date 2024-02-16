package service;
import chess.ChessGame;
import DataAccess.*;
import server.WebSocket.ResponseException;

import java.util.HashMap;
import java.util.UUID;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }


    public int createGame(String name) throws ResponseException {
        return gameDAO.createGameData(name);
    }
    public void joinGame() throws ResponseException {

    }
    public HashMap<Integer, GameData> listGames() throws ResponseException {
        return gameDAO.getGames();
    }
}
