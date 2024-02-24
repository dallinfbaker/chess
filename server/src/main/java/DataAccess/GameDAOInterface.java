package DataAccess;

//import chess.ChessGame;
import server.WebSocket.ResponseException;

import java.util.HashMap;

public interface GameDAOInterface {

    HashMap<Integer, GameData> getGames() throws ResponseException;
    GameData getGame(int gameID) throws ResponseException;
    int createGameData(String gameName) throws ResponseException;
    void addGame(GameData game) throws ResponseException;
    void clearGames() throws ResponseException;

}
