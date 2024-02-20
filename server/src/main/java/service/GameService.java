package service;
import DataAccess.*;
import server.WebSocket.ResponseException;

import java.util.HashMap;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }
    public int createGame(String name) {
        return gameDAO.createGameData(name);
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
    public GameData watchGame(int gameID) throws ResponseException {
        return gameDAO.getGame(gameID);
    }
    public HashMap<Integer, GameData> listGames() {
        return gameDAO.getGames();
    }
}
