package DataAccess;

import model.UserDataRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAODB implements UserDAOInterface {

    UserDAODB() {}

    @Override
    public UserDataRecord getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username, password, email FROM users WHERE username = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet rs = statement.executeQuery()) {
//                    String name = rs.getString(1);
//                    String word = rs.getString(2);
//                    String mail = rs.getString(3);
//                    return new UserDataRecord(name, word, mail);
                    if (rs.next()) {
                        var name = rs.getString(1);
                        var word = rs.getString(2);
                        var mail = rs.getString(3);
                        return new UserDataRecord(name, word, mail);
                    }
                }
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(2, email);
                statement.executeUpdate();
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void createUser(UserDataRecord user) throws DataAccessException { createUser(user.username(), user.password(), user.email()); }

    @Override
    public void clearUsers() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM " + "users";
            try (PreparedStatement statement = conn.prepareStatement(sql)) { statement.executeUpdate(); }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }
}
