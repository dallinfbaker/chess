package ui;

public class Client {
    private final String serverURL;
    private final String port;

    public Client (String URL, String port) {
        serverURL = URL;
        this.port = port;
    }

    public void run() {
        PreLoginEval preLoginEval = new PreLoginEval(null, serverURL, port);
        preLoginEval.loop();
    }
}
