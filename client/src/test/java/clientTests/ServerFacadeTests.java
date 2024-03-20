package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() {
        facade = new ServerFacade("http://localhost", String.format("%d", port));
    }

    @AfterEach
    public void post() { facade = null; }

    @AfterAll
    static void stopServer() { server.stop(); }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


}