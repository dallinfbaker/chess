package DataAccess;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private final ChessGame game;

    public GameData(int id) {
        gameID = id;
        game = new ChessGame();
    }

    public int getGameID() { return gameID; }

    public String getWhiteUsername() { return whiteUsername; }

    public void setWhiteUsername(String whiteUsername) { this.whiteUsername = whiteUsername; }

    public String getBlackUsername() { return blackUsername; }

    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername; }

    public String getGameName() { return gameName; }

    public void setGameName(String gameName) { this.gameName = gameName; }
}
