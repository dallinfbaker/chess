import ui.Client;
import ui.webSocket.NotificationHandler;

public class MainClient {

    public static void main(String[] args) {
        Client client = new Client("http://localhost", new NotificationHandler(), "8080");
        client.preLogin();
    }
}