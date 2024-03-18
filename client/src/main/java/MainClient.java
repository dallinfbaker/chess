import ui.Client;
import ui.webSocket.NotificationHandler;

import java.util.Objects;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
//        var serverUrl = "http://localhost:8080";

        Client client = new Client("http://localhost", new NotificationHandler(), "8080");

        client.preLogin();

//        while (true) {
//            System.out.printf("%n>>> ");
//            Scanner scanner = new Scanner(System.in);
//            String line = scanner.nextLine();
//            var output = client.eval(line);
//            if (Objects.equals(output, "quit")) {
//                System.out.print("exit program");
//                break;
//            }
//            System.out.printf("%s%n", output);
//        }
    }
}