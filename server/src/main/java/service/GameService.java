package service;
import chess.ChessGame;
import DataAccess.*;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }
}
