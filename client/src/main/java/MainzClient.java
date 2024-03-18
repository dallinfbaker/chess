import ui.Client;
import ui.webSocket.NotificationHandler;

import java.util.Scanner;

public class MainzClient {

    public static void main(String[] args) throws Exception {
        var serverUrl = "http://localhost:8080";

//        System.pause


        Client client = new Client("http://localhost", new NotificationHandler(), "8080");

        while (true) {
            System.out.printf("%n>>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var output = client.eval(line);
            System.out.printf("%s%n", output);
        }
    }
//    public void other(String[] args) {
//        var serverUrl = "http://localhost:8080";
////        server = new Server();
//        Client client = new Client("http://localhost", new NotificationHandler() {
//            @Override
//            public void notify(Notification notification) {
//
//            }
//        }, "8080") ;
//
//        if (args.length == 1) {
//            serverUrl = args[0];
//        }
//
//        new Repl(serverUrl).run();
//    }
//    @Override
/*    public void notify(Notification notification) {

    }*/
//    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
//    }
}