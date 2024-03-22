import ui.Client;
import ui.webSocket.ServerMessageHandler;

public class MainClient {

    public static void main(String[] args) {
        Client client = new Client("http://localhost", /*null,*/ "8080");
        client.run();
    }
}