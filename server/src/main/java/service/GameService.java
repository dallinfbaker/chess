package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import model.GameDataRecord;
import model.GameListRecord;
import exception.ResponseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameService {
    private final GameDAOInterface gameDAO;

    public GameService(GameDAOInterface gameDAO) { this.gameDAO = gameDAO; }
    public Map<String, Integer> createGame(String name) throws ResponseException {
        if (Objects.isNull(name)) throw new ResponseException(400, "Error: bad request");
        try {
            int id = gameDAO.createGameData(name);
            Map<String, Integer> gameId = new HashMap<>();
            gameId.put("gameID", id);
            return gameId;
        }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request");}
    }
    public void joinGame(int gameID, String playerColor, String username) throws ResponseException {
        GameDataRecord gameData;
        String user;
        try { gameData = gameDAO.getGame(gameID); }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        if (Objects.equals(playerColor.toUpperCase(), "WHITE")) {
            try {
                user = gameData.whiteUsername();
                if (Objects.isNull(user)) gameDAO.setWhiteUsername(gameID, username);
                else if (!Objects.equals(user, username)) throw new ResponseException(403, "Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        }
        else if (Objects.equals(playerColor.toUpperCase(), "BLACK")) {
            try {
                user = gameData.blackUsername();
                if (Objects.isNull(user)) gameDAO.setBlackUsername(gameID, username);
                else if (!Objects.equals(user, username)) throw new ResponseException(403,"Error: already taken");
            } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        }
    }
    public void watchGame(int gameID, String auth) throws ResponseException {
        try {
            GameDataRecord game = gameDAO.getGame(gameID);
            game.observers().observers().add(auth);
            gameDAO.updateGame(game);
        }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
    public GameListRecord listGames() throws ResponseException {
        try { return gameDAO.getGames(); }
        catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
    public GameDataRecord makeMove(int gameId, ChessMove move, String username) throws ResponseException {
        try {
            GameDataRecord game = gameDAO.getGame(gameId);
            if(game.game().getFinished()) throw new ResponseException(401, "Error: cannot move, game has ended");
            ChessGame.TeamColor playerColor = getPlayerColor(game, username);
            ChessGame.TeamColor moveColor = game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor();
            if (!Objects.equals(playerColor, moveColor)) throw new ResponseException(401, "Error: cannot move opponent's piece");
            game.game().makeMove(move);
            gameDAO.updateGame(game);
            return game;
        } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
        catch (InvalidMoveException e) { throw new ResponseException(403, "Error: illegal move"); }
    }
    public void removePlayer(int gameID, String username) throws ResponseException {
        try {
            GameDataRecord game = gameDAO.getGame(gameID);
            if (Objects.equals(game.whiteUsername(), username)) gameDAO.setWhiteUsername(gameID, null);
            else if (Objects.equals(game.blackUsername(), username)) gameDAO.setBlackUsername(gameID, null);
            else {
                game.observers().observers().remove(username);
                gameDAO.updateGame(game);
            }
        } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
    public void resignGame(int gameID, String username) throws ResponseException {
        try {
            GameDataRecord game = gameDAO.getGame(gameID);
            if(game.game().getFinished()) throw new ResponseException(401, "Error: cannot resign, game has ended");
            ChessGame.TeamColor playerColor = getPlayerColor(game, username);
            game.game().resign(playerColor);
            gameDAO.updateGame(game);
        } catch (DataAccessException e) { throw new ResponseException(400, "Error: bad request"); }
    }
    public GameDataRecord getGame(int gameID) throws ResponseException {
        try { return gameDAO.getGame(gameID); }
        catch (DataAccessException e) { throw new ResponseException(400, e.getMessage()); }
    }

    private ChessGame.TeamColor getPlayerColor(GameDataRecord game, String username) throws ResponseException {
        ChessGame.TeamColor playerColor = Objects.equals(username, game.whiteUsername()) ? ChessGame.TeamColor.WHITE : Objects.equals(username, game.blackUsername()) ? ChessGame.TeamColor.BLACK : null;
        if(Objects.isNull(playerColor)) throw new ResponseException(401, "not a player");
        return playerColor;
    }
}
