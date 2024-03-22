import ui.Client;

public class MainClient {

    public static void main(String[] args) {
        Client client = new Client("http://localhost", "8080");
        client.run();
    }
}