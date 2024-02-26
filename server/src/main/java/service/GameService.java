package service;

import DataAccess.DataAccessException;
import DataAccess.GameDAO;
import model.GameDataRecord;
import server.WebSocket.ResponseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) { this.gameDAO = gameDAO; }
    public Map<String, Integer> createGame(String name) throws ResponseException {
        if (Objects.isNull(name)) throw new ResponseException(400, "Error: bad request");
        int id = gameDAO.createGameData(name);
        Map<String, Integer> gameId = new HashMap<>();
        gameId.put("gameID", id);
        return gameId;
    }
    public void joinGame(int gameID, String playerColor, String username) throws ResponseException {
        GameDataRecord gameData = gameDAO.getGame(gameID);
        if (Objects.equals(playerColor, "WHITE")) {
            try {
                if (Objects.isNull(gameData.whiteUsername())) gameDAO.setWhiteUsername(gameID, username);
                else if (!Objects.equals(gameData.whiteUsername(), username))
                    throw new ResponseException(403, "Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(401, "Error: bad request"); }
        }
        else {
            try {
                if (Objects.isNull(gameData.blackUsername())) gameDAO.setBlackUsername(gameID, username);
                else if (!Objects.equals(gameData.blackUsername(), username)) throw new ResponseException(403,"Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(401, "Error: bad request"); }
        }
    }
    public void watchGame(int gameID) throws ResponseException { gameDAO.getGame(gameID); }
    public HashMap<Integer, GameDataRecord> listGames() { return gameDAO.getGames(); }
}
