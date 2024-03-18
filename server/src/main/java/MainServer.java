import server.Server;

public class MainServer {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(8080);

        System.out.printf("%d", port);
    }
}