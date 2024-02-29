package dataAccess;

import model.AuthDataRecord;
import model.GameDataRecord;
import model.UserDataRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.function.Function;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static public Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static public void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS);
            prepareStatement(ps, params).executeUpdate();
        } catch (SQLException e) { throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage())); }
    }

    static public <T> Collection<T> executeQuery(String statement, Function<ResultSet, T> builder, Object... params) throws DataAccessException {
        Collection<T> list = new ArrayList<>();
        try (var conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS);
            ResultSet rs = prepareStatement(ps, params).executeQuery();
            while (rs.next()) {list.add(builder.apply(rs)); }
            return list;
        } catch (SQLException e) { throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage())); }
    }

    private static PreparedStatement prepareStatement(PreparedStatement ps, Object... params) throws SQLException {
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            switch (param) {
                case String p -> ps.setString(i + 1, p);
                case Integer p -> ps.setInt(i + 1, p);
                case UserDataRecord p -> ps.setString(i + 1, p.toString());
                case GameDataRecord p -> ps.setString(i + 1, p.toString());
                case AuthDataRecord p -> ps.setString(i + 1, p.toString());
                case null -> ps.setNull(i + 1, NULL);
                default -> {}
            }
        }
        return ps;
    }
}