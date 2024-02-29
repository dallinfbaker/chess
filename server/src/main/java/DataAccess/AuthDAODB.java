package DataAccess;

import model.AuthDataRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class AuthDAODB  implements AuthDAOInterface {

    AuthDAODB() {}

    @Override
    public AuthDataRecord createAuthToken(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthDataRecord authData = new AuthDataRecord(token, username);
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, authData.authToken());
                statement.setString(2, authData.username());
                statement.executeUpdate();
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }

        return authData;
    }

    @Override
    public AuthDataRecord getAuth(String token) throws DataAccessException {
        try (Connection conn =  DatabaseManager.getConnection()) {
            String sql = "SELECT username FROM auth_tokens WHERE token = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, token);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) { return new AuthDataRecord(token, rs.getString(1)); }
                }
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
        return null;
    }

    @Override
    public boolean validAuth(String token) throws DataAccessException { return Objects.isNull(getAuth(token)); }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM auth_tokens WHERE token = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, token);
                statement.executeUpdate();
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM " + "auth_tokens";
            try (PreparedStatement statement = conn.prepareStatement(sql)) { statement.executeUpdate(); }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void addAuth(String number, String myUser) {
        AuthDataRecord authData = new AuthDataRecord(number, myUser);
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, authData.authToken());
                statement.setString(2, authData.username());
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ignored) {}
    }
}
