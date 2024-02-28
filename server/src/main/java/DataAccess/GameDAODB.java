package DataAccess;

import model.GameDataRecord;
import server.WebSocket.ResponseException;

import java.util.HashMap;

public class GameDAODB implements GameDAOInterface {
    @Override
    public HashMap<Integer, GameDataRecord> getGames() throws DataAccessException {
        return null;
    }

    @Override
    public GameDataRecord getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public int createGameData(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public void setWhiteUsername(int gameID, String username) throws DataAccessException {

    }

    @Override
    public void setBlackUsername(int gameID, String username) throws DataAccessException {

    }

    @Override
    public void clearGames() throws DataAccessException {

    }
}
