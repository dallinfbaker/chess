package DataAccess;
import chess.ChessGame;

import java.util.*;

public class GameDAO implements GameDAOInterface {

    private HashMap<Integer, GameData> games;

    public GameDAO(){
        games = new HashMap<>();
    }

    private int generateID(ChessGame game) {
//        UUID uuidFromString = UUID.fromString(game.toString());
        return game.hashCode();
    }

    public int createGameData(ChessGame game, String whiteUsername){
        int gameID = game.hashCode();
        GameData gameData = new GameData(gameID, game);
        gameData.setWhiteUsername(whiteUsername);
        games.put(gameID, gameData);
        return gameID;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public void addGame(GameData game) {
        games.put(generateID(game.getGame()), game);
    }

    public void updateGame(int gameID, GameData game) {
        games.replace(gameID, game);
    }

    public void deleteGame(int gameID) {
        games.remove(gameID);
    }

    public void clearGames() {
        games = new HashMap<>();
    }
}
