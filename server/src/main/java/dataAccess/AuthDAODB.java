package dataAccess;

import model.AuthDataRecord;
import model.UserDataRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthDAODB  implements AuthDAOInterface {

    Function<ResultSet, AuthDataRecord> buildAuth = rs -> {
        try {
            var token = rs.getString(1);
            var username = rs.getString(2);
            return new AuthDataRecord(token, username);
        } catch (SQLException ignored) { return null; }
    };

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
        try { return (AuthDataRecord) DatabaseManager.executeQuery(statement, buildAuth, token).toArray()[0]; }
        catch (ArrayIndexOutOfBoundsException e) { throw new DataAccessException(e.getMessage()); }
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
        String statement = "TRUNCATE TABLE auth_tokens";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addAuth(String number, String username) {
        String token = UUID.randomUUID().toString();
        String statement = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
        try { DatabaseManager.executeUpdate(statement, token, username); }
        catch (DataAccessException ignored) {}
    }
}
