package ui;

import java.util.*;

public abstract class EvalLoop {

    protected static ServerFacade server;

    protected static String serverUrl, port, userName, authToken;

    protected EvalLoop(ServerFacade serverFacade, String serverURL, String port) {
        server = serverFacade;
        serverUrl = serverURL;
        EvalLoop.port = port;
    }

    void connect(String serverURL, String port) { if (Objects.isNull(server)) server = new ServerFacade(serverURL, port); }



    public Collection<String> getInput() {
        ArrayList<String> output = new ArrayList<>();
        System.out.printf("%n>>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        output.add(line.toLowerCase().split(" ")[0]);
        var tokens = line.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        output.add(eval(cmd, params));
        return output;
    }

    public abstract String eval(String cmd, String... params);

    public abstract String loop();

    public abstract String help();
}
