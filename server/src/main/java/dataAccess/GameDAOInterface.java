package dataAccess;

import model.GameDataRecord;
import model.GameListRecord;

public interface GameDAOInterface {

    GameListRecord getGames() throws DataAccessException;
    GameDataRecord getGame(int gameId) throws DataAccessException;
    int createGameData(String gameName) throws DataAccessException;
    void setWhiteUsername(int gameId, String username) throws DataAccessException;
    void setBlackUsername(int gameId, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;
    void addGame(GameDataRecord data);
    void updateGame(GameDataRecord data);
}
