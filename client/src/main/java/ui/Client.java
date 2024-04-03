package ui;

public class Client {
    private final String serverURL;
    private final String port;

    public Client (String url, String port) {
        serverURL = url;
        this.port = port;
    }

    public void run() {
        PreLoginEval preLoginEval = new PreLoginEval(null, serverURL, port);
        preLoginEval.loop();
    }
}
