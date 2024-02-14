package DataAccess;

import chess.ChessGame;

public interface GameDAOInterface {

    GameData getGame(int gameID);
    int createGameData(ChessGame game, String whiteUsername);
    void addGame(GameData game);
    void updateGame(int gameID, GameData game);
    void deleteGame(int gameID);
    void clearGames();

}
