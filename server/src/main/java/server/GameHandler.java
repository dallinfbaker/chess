package server;

import DataAccess.DAOManager;
import DataAccess.GameDAO;
import DataAccess.GameData;
import chess.ChessGame;
import server.WebSocket.Connection;
import server.WebSocket.ResponseException;
import service.GameService;

import java.util.*;

public class GameHandler {
    private final GameService gameService;
    public GameHandler(GameDAO gameDAO) {
        gameService = new GameService(gameDAO);
    }

    public Map<String, Integer> createGame(String gameName) throws ResponseException {
        int id = gameService.createGame(gameName);
        Map<String, Integer> gameId = new HashMap<>();
        gameId.put("gameID", id);
        return gameId;
    }
    public void joinGame() throws ResponseException {

    }

    public HashMap<Integer, GameData> listGames() throws ResponseException {
        return gameService.listGames();

    }
}
