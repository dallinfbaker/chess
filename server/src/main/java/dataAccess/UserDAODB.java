package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameDataRecord;
import model.UserDataRecord;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDAODB implements UserDAOInterface {

    Function<ResultSet, UserDataRecord> buildUser = rs -> {
        try {
            var name = rs.getString(1);
            var word = rs.getString(2);
            var mail = rs.getString(3);
            return new UserDataRecord(name, word, mail);
        } catch (SQLException ignored) { return null; }
    };

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public UserDataRecord getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM users WHERE username = ?";
        try { return (UserDataRecord) DatabaseManager.executeQuery(statement, buildUser, username).toArray()[0]; }
        catch (ArrayIndexOutOfBoundsException e) { throw new DataAccessException(e.getMessage()); }
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, username, encryptPassword(password), email);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String statement = "TRUNCATE TABLE users";
        DatabaseManager.executeUpdate(statement);
    }
}
