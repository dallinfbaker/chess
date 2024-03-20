package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthDataRecord;
import model.GameDataRecord;
import model.GameListRecord;
import model.UserDataRecord;
import org.junit.jupiter.api.*;
import server.Server;
import ui.JoinGameData;
import ui.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;
    private static int port;
    private final UserDataRecord user = new UserDataRecord("u", "p", "e");
    private AuthDataRecord auth;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() throws ResponseException {
        facade = new ServerFacade("http://localhost", String.format("%d", port));
        facade.clearDatabase();
        auth = facade.registerUser(user);
        facade.createGame(auth, new GameDataRecord(33, null, null, "my game", new ChessGame()));
    }

    @AfterEach
    void logout() throws ResponseException { facade.logout(auth); }

    @AfterAll
    static void stopServer() { server.stop(); }

    @Test
    public void clearDatabasePos() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.clearDatabase());
        Assertions.assertDoesNotThrow(() -> facade.clearDatabase());
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> auth = facade.registerUser(user));
        Assertions.assertTrue(facade.listGames(auth).games().isEmpty());
        Assertions.assertTrue(true);
    }
//    @Test public void clearDatabaseNeg() { Assertions.assertTrue(true); }
    @Test
    public void registerUserPos() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserDataRecord("ddd", "ddd", "ddd")));
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserDataRecord("aaa", "ddd", "i")));
    }
    @Test
    public void registerUserNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(new UserDataRecord("a", "", "e")));
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(new UserDataRecord("", "", "")));
    }
    @Test
    public void loginPos() { Assertions.assertDoesNotThrow(() -> facade.login(user)); }
    @Test
    public void loginNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new UserDataRecord("sfe", "adhhdsfh", "sfe")));
        Assertions.assertThrows(ResponseException.class, () -> facade.login(new UserDataRecord("sdg", "", "ee")));
    }
    @Test
    public void logoutPos() { Assertions.assertDoesNotThrow(() -> facade.logout(facade.login(user))); }
    @Test
    public void logoutNeg() {
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(new AuthDataRecord("", "")));
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(new AuthDataRecord("auth token", "u")));
    }
    @Test
    public void listGamesPos() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.listGames(auth));
        GameListRecord games = facade.listGames(auth);
        Assertions.assertInstanceOf(Integer.class, ((GameDataRecord)games.games().toArray()[0]).gameID());
    }
    @Test
    public void listGamesNeg() { Assertions.assertThrows(ResponseException.class, () -> facade.listGames(new AuthDataRecord("sdf", "u"))); }
    @Test
    public void createGamePos() { Assertions.assertDoesNotThrow(() -> facade.createGame(auth, new GameDataRecord(1, null, null, "null", new ChessGame()))); }
    @Test
    public void createGameNeg() { Assertions.assertThrows(ResponseException.class, () -> facade.createGame(auth, new GameDataRecord(1, null, null, null, null))); }
    @Test
    public void joinGamePos() throws ResponseException {
        int gameId = ((GameDataRecord)facade.listGames(auth).games().toArray()[0]).gameID();
        Assertions.assertDoesNotThrow(() -> facade.joinGame(auth, new JoinGameData(gameId, null)));
        Assertions.assertDoesNotThrow(() -> facade.joinGame(auth, new JoinGameData(gameId, "white")));
    }
    @Test
    public void joinGameNeg() { Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(auth, new JoinGameData(1, null))); }
}