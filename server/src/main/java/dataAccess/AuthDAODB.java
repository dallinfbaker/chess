package dataAccess;

import model.AuthDataRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthDAODB  implements AuthDAOInterface {

    private AuthDataRecord buildAuth(ResultSet rs) throws SQLException {
        rs.next();
        var token = rs.getString(1);
        var username = rs.getString(2);
        return new AuthDataRecord(token, username);
    }

    @Override
    public AuthDataRecord createAuthToken(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        String statement = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, token, username);
        return new AuthDataRecord(token, username);
    }

    @Override
    public AuthDataRecord getAuth(String token) throws DataAccessException {
        String statement = "SELECT token, username FROM auth_tokens WHERE token = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ResultSet rs = DatabaseManager.prepareStatement(ps, token).executeQuery();
                return buildAuth(rs);
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public boolean validAuth(String token) {
        try {
            getAuth(token);
            return true;
        } catch(DataAccessException ignored) { return false; }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        String statement = "DELETE FROM auth_tokens WHERE token = ?";
        DatabaseManager.executeUpdate(statement, token);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "TRUNCATE TABLE auth_tokens";
            try (PreparedStatement statement = conn.prepareStatement(sql)) { statement.executeUpdate(); }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void addAuth(String number, String username) {
        String token = UUID.randomUUID().toString();
        String statement = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
        try { DatabaseManager.executeUpdate(statement, token, username); }
        catch (DataAccessException ignored) {}
    }
}
