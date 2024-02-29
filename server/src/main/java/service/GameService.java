package service;

import DataAccess.DataAccessException;
import DataAccess.GameDAOInterface;
import model.GameDataRecord;
import model.GameListRecord;
import server.WebSocket.ResponseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameService {
    private final GameDAOInterface gameDAO;

    public GameService(GameDAOInterface gameDAO) { this.gameDAO = gameDAO; }
    public Map<String, Integer> createGame(String name) throws ResponseException {
        if (Objects.isNull(name)) throw new ResponseException(400, "Error: bad request");
        try {
            int id = gameDAO.createGameData(name);
            Map<String, Integer> gameId = new HashMap<>();
            gameId.put("gameID", id);
            return gameId;
        }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request");}
    }
    public void joinGame(int gameID, String playerColor, String username) throws ResponseException {
        GameDataRecord gameData;
        String user;
        try { gameData = gameDAO.getGame(gameID); }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        if (Objects.equals(playerColor, "WHITE")) {
            try {
                user = gameData.whiteUsername();
                if (Objects.isNull(user)) gameDAO.setWhiteUsername(gameID, username);
                else if (!Objects.equals(user, username))
                    throw new ResponseException(403, "Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        }
        else {
            try {
                user = gameData.blackUsername();
                if (Objects.isNull(user)) gameDAO.setBlackUsername(gameID, username);
                else if (!Objects.equals(user, username)) throw new ResponseException(403,"Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(401, "Error: bad request"); }
        }
    }
    public void watchGame(int gameID) throws ResponseException {
        try { gameDAO.getGame(gameID); }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
    public GameListRecord listGames() throws ResponseException {
        try { return gameDAO.getGames(); }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
}
