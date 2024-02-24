package service;

import DataAccess.GameDAO;
import DataAccess.GameData;
import server.WebSocket.ResponseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) { this.gameDAO = gameDAO; }
    public Map<String, Integer> createGame(String name) throws ResponseException {
        if (Objects.isNull(name)) throw new ResponseException(400, "Error: Bad Request");
        int id = gameDAO.createGameData(name);
        Map<String, Integer> gameId = new HashMap<>();
        gameId.put("gameID", id);
        return gameId;
    }
    public void joinGame(int gameID, String playerColor, String username) throws ResponseException {
        GameData gameData = gameDAO.getGame(gameID);
        if (Objects.equals(playerColor, "WHITE")) {
            if (Objects.isNull(gameData.getWhiteUsername())) gameData.setWhiteUsername(username);
            else if (!Objects.equals(gameData.getWhiteUsername(), username)) throw new ResponseException(403,"Error: already taken");
        }
        else {
            if (Objects.isNull(gameData.getBlackUsername())) gameData.setBlackUsername(username);
            else if (!Objects.equals(gameData.getBlackUsername(), username)) throw new ResponseException(403,"Error: already taken");
        }
    }
    public void watchGame(int gameID) throws ResponseException { gameDAO.getGame(gameID); }
    public HashMap<Integer, GameData> listGames() { return gameDAO.getGames(); }
}
