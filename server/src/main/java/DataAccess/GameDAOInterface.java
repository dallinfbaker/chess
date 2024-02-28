package DataAccess;

import model.GameDataRecord;
import java.util.HashMap;

public interface GameDAOInterface {

    HashMap<Integer, GameDataRecord> getGames() throws DataAccessException;
    GameDataRecord getGame(int gameID) throws DataAccessException;
    int createGameData(String gameName) throws DataAccessException;
    void setWhiteUsername(int gameID, String username) throws DataAccessException;
    void setBlackUsername(int gameID, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;

}
