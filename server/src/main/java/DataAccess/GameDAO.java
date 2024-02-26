package DataAccess;

import chess.ChessGame;
import model.GameDataRecord;
import server.WebSocket.ResponseException;
import java.util.*;

public class GameDAO implements GameDAOInterface {

    private HashMap<Integer, GameDataRecord> games;

    public GameDAO(){ games = new HashMap<>(); }

    private int generateID() {
        int id = UUID.randomUUID().hashCode();
        id = Math.abs(id);
        id = id % 10000;
        return id;
    }

    public int createGameData(String gameName) {
        int id = generateID();
        GameDataRecord gameData = new GameDataRecord(id, null, null, gameName, new ChessGame());
        games.put(id, gameData);
        return id;
    }

    public void setWhiteUsername(int gameID, String username) throws DataAccessException {
        GameDataRecord game = games.get(gameID);
        if (Objects.isNull(game)) throw new DataAccessException("Error: invalid id");
        games.put(game.gameID(), new GameDataRecord(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
    }

    public void setBlackUsername(int gameID, String username) throws DataAccessException {
        GameDataRecord game = games.get(gameID);
        if (Objects.isNull(game)) throw new DataAccessException("Error: invalid id");
        games.put(game.gameID(), new GameDataRecord(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
    }

    public HashMap<Integer, GameDataRecord> getGames() { return games; }

    public GameDataRecord getGame(int gameID) throws ResponseException {
        if (!games.containsKey(gameID)) throw new ResponseException(400, "Error: bad request");
        return games.get(gameID);
    }

    public void addGame(GameDataRecord game) { games.put(game.gameID(), new GameDataRecord(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game())); }

    public void clearGames() { games = new HashMap<>(); }
}
