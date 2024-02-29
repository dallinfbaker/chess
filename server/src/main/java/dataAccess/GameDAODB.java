package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameDataRecord;
import model.GameListRecord;

import java.sql.*;
import java.util.UUID;
import java.util.function.Function;

public class GameDAODB implements GameDAOInterface {

    Function<ResultSet, GameDataRecord> buildGame = rs -> {
        try {
            int gameId = rs.getInt(1);
            String white = rs.getString(2);
            String black = rs.getString(3);
            String name = rs.getString(4);
            ChessGame game = new Gson().fromJson(rs.getString(5), ChessGame.class);
            return new GameDataRecord(gameId, white, black, name, game);
        } catch (SQLException ignored) { return null; }
    };

    private int generateID() {
        int id = UUID.randomUUID().hashCode();
        id = Math.abs(id);
        id = id % 10000;
        return id;
    }

    @Override
    public GameListRecord getGames() throws DataAccessException {
        String statement = "SELECT * FROM chess_games";
        return new GameListRecord(DatabaseManager.executeQuery(statement, buildGame));
    }

    @Override
    public GameDataRecord getGame(int gameID) throws DataAccessException {
        String statement = "SELECT game_id, white_player_username, black_player_username, game_name, game_json FROM chess_games WHERE game_id = ?";
        try { return (GameDataRecord) DatabaseManager.executeQuery(statement, buildGame, gameID).toArray()[0]; }
        catch (ArrayIndexOutOfBoundsException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public int createGameData(String gameName) throws DataAccessException {
        String statement = "INSERT INTO chess_games (game_id, game_name, game_json) VALUES (?, ?, ?)";
        int id = generateID();
        var gameJson = new Gson().toJson(new ChessGame());
        DatabaseManager.executeUpdate(statement, id, gameName, gameJson);
        return id;
    }

    @Override
    public void setWhiteUsername(int gameID, String username) throws DataAccessException {
        String statement = "UPDATE chess_games Set white_player_username = ? WHERE game_id = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }

    @Override
    public void setBlackUsername(int gameID, String username) throws DataAccessException {
        String statement = "UPDATE chess_games Set black_player_username = ? WHERE game_id = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }

    @Override
    public void clearGames() throws DataAccessException {
        String statement = "TRUNCATE TABLE chess_games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addGame(GameDataRecord data) {
        String statement = "INSERT INTO chess_games (game_id, white_player_username, black_player_username, game_name, game_json) VALUES (?, ?, ?, ?, ?)";
        try { DatabaseManager.executeUpdate(statement, data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), new Gson().toJson(data.game())); }
        catch (DataAccessException ignored) {}
    }
}
