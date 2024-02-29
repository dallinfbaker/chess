package dataAccess;

import model.UserDataRecord;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDAODB implements UserDAOInterface {

    UserDAODB() {}

    private UserDataRecord buildUser(ResultSet rs) throws SQLException {
        rs.next();
        var name = rs.getString(1);
        var word = rs.getString(2);
        var mail = rs.getString(3);
        return new UserDataRecord(name, word, mail);
    }

    @Override
    public UserDataRecord getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ResultSet rs = DatabaseManager.prepareStatement(ps, username).executeQuery();
                return buildUser(rs);
            }
        } catch (SQLException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, username, password, email);
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
