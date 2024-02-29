package DataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameDataRecord;
import model.GameListRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class GameDAODB implements GameDAOInterface {

    private int generateID() {
        int id = UUID.randomUUID().hashCode();
        id = Math.abs(id);
        id = id % 10000;
        return id;
    }

    @Override
    public GameListRecord getGames() throws DataAccessException {
        Collection<GameDataRecord> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM chess_games";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int gameId = rs.getInt("game_id");
                        String white = rs.getString(1);
                        String black = rs.getString(2);
                        String name = rs.getString(3);
                        ChessGame game = new Gson().fromJson(rs.getString(4), ChessGame.class);

                        GameDataRecord data = new GameDataRecord(gameId, white, black, name, game);
                        games.add(data);
                    }
                }
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
        return new GameListRecord(games);
    }

    @Override
    public GameDataRecord getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT white_player_username, black_player_username, game_name, game_json FROM chess_games WHERE game_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, gameID);
                try (ResultSet rs = statement.executeQuery()) {
                    String white = rs.getString(1);
                    String black = rs.getString(2);
                    String name = rs.getString(3);
                    ChessGame game = new Gson().fromJson(rs.getString(4), ChessGame.class);
                    return new GameDataRecord(gameID, white, black, name, game);
                }
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public int createGameData(String gameName) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO chess_games (game_id, game_name, game_json) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int id = generateID();
                statement.setInt(1, id);
                statement.setString(2, gameName);
                statement.setString(3, new Gson().toJson(new ChessGame()));
                statement.executeUpdate();
                return id;
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void setWhiteUsername(int gameID, String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE chess_games Set white_player_username = ? WHERE game_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setInt(2, gameID);
                statement.executeUpdate();
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void setBlackUsername(int gameID, String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE chess_games Set black_player_username = ? WHERE game_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setInt(2, gameID);
                statement.executeUpdate();
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void clearGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM " + "chess_games";
            try (PreparedStatement statement = conn.prepareStatement(sql)) { statement.executeUpdate(); }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void addGame(GameDataRecord data) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO chess_games (game_id, white_player_username, black_player_username, game_name, game_json) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, data.gameID());
                statement.setString(2, data.whiteUsername());
                statement.setString(3, data.blackUsername());
                statement.setString(4, data.gameName());
                statement.setString(5, new Gson().toJson(data.game()));
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {}
    }
}
