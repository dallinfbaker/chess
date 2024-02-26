package DataAccess;

import model.GameDataRecord;
import server.WebSocket.ResponseException;
import java.util.HashMap;

public interface GameDAOInterface {

    HashMap<Integer, GameDataRecord> getGames() throws ResponseException;
    GameDataRecord getGame(int gameID) throws ResponseException;
    int createGameData(String gameName) throws ResponseException;
    void setWhiteUsername(int gameID, String username) throws DataAccessException;
    void setBlackUsername(int gameID, String username) throws DataAccessException;
    void clearGames() throws ResponseException;

}
