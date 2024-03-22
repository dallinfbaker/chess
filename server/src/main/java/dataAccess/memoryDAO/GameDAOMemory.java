package dataAccess.memoryDAO;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import model.GameDataRecord;
import model.GameListRecord;
import model.ObservingUsers;

import java.util.*;

public class GameDAOMemory implements GameDAOInterface {

    private HashMap<Integer, GameDataRecord> games;

    public GameDAOMemory(){ games = new HashMap<>(); }

    private int generateID() {
        int id = UUID.randomUUID().hashCode();
        id = Math.abs(id);
        id = id % 10000;
        return id;
    }

    public int createGameData(String gameName) {
        int id = generateID();
        GameDataRecord gameData = new GameDataRecord(id, null, null, gameName, new ChessGame(), new ObservingUsers(new HashSet<>()));
        games.put(id, gameData);
        return id;
    }

    public void setWhiteUsername(int gameID, String username) throws DataAccessException {
        GameDataRecord game = games.get(gameID);
        if (Objects.isNull(game)) throw new DataAccessException("Error: invalid id");
        games.put(game.gameID(), new GameDataRecord(game.gameID(), username, game.blackUsername(), game.gameName(), game.game(), new ObservingUsers(new HashSet<>())));
    }

    public void setBlackUsername(int gameID, String username) throws DataAccessException {
        GameDataRecord game = games.get(gameID);
        if (Objects.isNull(game)) throw new DataAccessException("Error: invalid id");
        games.put(game.gameID(), new GameDataRecord(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game(), new ObservingUsers(new HashSet<>())));
    }

    public GameListRecord getGames() { return new GameListRecord(games.values()); }

    public GameDataRecord getGame(int gameId) throws DataAccessException {
        if (!games.containsKey(gameId)) throw new DataAccessException("Error: bad request");
        return games.get(gameId);
    }

    public void addGame(GameDataRecord game) { games.put(game.gameID(), new GameDataRecord(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), new ObservingUsers(new HashSet<>()))); }

    public void updateGame(GameDataRecord data) { games.put(data.gameID(), data); }

    public void clearGames() { games = new HashMap<>(); }
}
