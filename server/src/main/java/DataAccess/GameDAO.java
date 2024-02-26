package DataAccess;

import model.GameData;
import server.WebSocket.ResponseException;
import java.util.*;

public class GameDAO implements GameDAOInterface {

    private HashMap<Integer, GameData> games;

    public GameDAO(){ games = new HashMap<>(); }

    private int generateID() {
        int id = UUID.randomUUID().hashCode();
        id = Math.abs(id);
        id = id % 10000;
        return id;
    }

    public int createGameData(String gameName) {
        int id = generateID();
        GameData gameData = new GameData(id);
        gameData.setGameName(gameName);
        games.put(id, gameData);
        return id;
    }

    public HashMap<Integer, GameData> getGames() { return games; }

    public GameData getGame(int gameID) throws ResponseException {
        if (!games.containsKey(gameID)) throw new ResponseException(400, "Error: bad request");
        return games.get(gameID);
    }

    public void addGame(GameData game) { games.put(game.getGameID(), game); }

    public void clearGames() { games = new HashMap<>(); }
}
