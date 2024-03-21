package ui;


import ui.webSocket.NotificationHandler;
import ui.webSocket.WebSocketFacade;

public class Client {
//    private final ServerFacade server = null;
    private final String serverURL;
    private final String port;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    public Client (String URL, NotificationHandler nh, String port) {
        serverURL = URL;
        this.port = port;
        notificationHandler = nh;
    }

    public void run() {
        PreLoginEval preLoginEval = new PreLoginEval(null, serverURL, port);
        preLoginEval.loop();
    }
}
