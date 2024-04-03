package dataAccess.databaseDAO;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAOInterface;
import model.GameDataRecord;
import model.GameListRecord;
import model.ObservingUsers;

import java.sql.*;
import java.util.HashSet;
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
            ObservingUsers users = new Gson().fromJson(rs.getString(6), ObservingUsers.class);
            return new GameDataRecord(gameId, white, black, name, game, users);
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
    public GameDataRecord getGame(int gameId) throws DataAccessException {
        String statement = "SELECT game_id, white_player_username, black_player_username, game_name, game_json, observers_json FROM chess_games WHERE game_id = ?";
        try { return (GameDataRecord) DatabaseManager.executeQuery(statement, buildGame, gameId).toArray()[0]; }
        catch (ArrayIndexOutOfBoundsException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public int createGameData(String gameName) throws DataAccessException {
        String statement = "INSERT INTO chess_games (game_id, game_name, game_json, observers_json) VALUES (?, ?, ?, ?)";
        int id = generateID();
        var gameJson = new Gson().toJson(new ChessGame());
        var observersJson = new Gson().toJson(new ObservingUsers(new HashSet<>()));
        DatabaseManager.executeUpdate(statement, id, gameName, gameJson, observersJson);
        return id;
    }

    @Override
    public void setWhiteUsername(int gameID, String username) throws DataAccessException {
        String statement = "UPDATE chess_games SET white_player_username = ? WHERE game_id = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }

    @Override
    public void setBlackUsername(int gameID, String username) throws DataAccessException {
        String statement = "UPDATE chess_games SET black_player_username = ? WHERE game_id = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }

    @Override
    public void clearGames() throws DataAccessException {
        String statement = "TRUNCATE TABLE chess_games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addGame(GameDataRecord data) {
        String statement = "INSERT INTO chess_games (game_id, white_player_username, black_player_username, game_name, game_json, observers_json) VALUES (?, ?, ?, ?, ?, ?)";
        try { DatabaseManager.executeUpdate(statement, data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), new Gson().toJson(data.game()), new Gson().toJson(data.observers())); }
        catch (DataAccessException ignored) {}
    }

    @Override
    public void updateGame(GameDataRecord data) {
        String statement = "UPDATE chess_games SET game_json = ?, observers_json = ? WHERE game_id = ?";
//        try { DatabaseManager.executeUpdate(statement, new Gson().toJson(data.game()), new Gson().toJson(data.observers()), data.gameID()); }
        try {
            ChessGame game = data.game();
            ObservingUsers observingUsers = data.observers();
            var gameData = new Gson().toJson(game);
            var observers = new Gson().toJson(observingUsers);
            var id = data.gameID();
            DatabaseManager.executeUpdate(statement, gameData, observers, id);
        }
        catch (DataAccessException ignored) {}
        catch (Throwable thrown) {
            System.out.println("couldn't write to database");
            System.err.println(thrown.getMessage());
        }
    }
}
