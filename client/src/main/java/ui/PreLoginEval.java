package ui;

import exception.ResponseException;

import java.util.Iterator;
import java.util.Objects;

public class PreLoginEval extends EvalLoop {

    protected PreLoginEval(ServerFacade serverFacade, String serverURL, String port) { super(serverFacade, serverURL, port); }
    @Override
    public String eval(String cmd, String... params) {
        try {
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) { return e.getMessage(); }
    }

    @Override
    public String loop() {
        String input, output;
        do {
            Iterator<String> inputs = getInput().iterator();
            input = inputs.next();
            output = inputs.next();
            System.out.printf("%s%n", output);
            if (Objects.equals(input, "login") || Objects.equals(input, "register") &&
                    (Objects.equals(output.split(" ")[0], "Logged") || Objects.equals(output.split(" ")[0], "Registered"))) {
                PostLoginEval postLoginEval = new PostLoginEval(server, serverUrl, port);
                output = postLoginEval.loop();
            }
            if (Objects.equals(output, "quit")) { break; }
        } while (!Objects.equals(output, "quit"));
        System.out.print("exit program");
        return null;
    }

    public String login(String... params) throws ResponseException {
        connect(serverUrl, port);
        userName = params[0];
        authToken = server.login(params).authToken();
        return "Logged in";
    }
    public String register(String... params) throws ResponseException {
        connect(serverUrl, port);
        userName = params[0];
        authToken = server.registerUser(params).authToken();
        return "Registered";
    }

    @Override
    public String help() {
        return """
            register <USERNAME> <PASSWORD> <EMAIL> - to create an account
            login <USERNAME> <PASSWORD> - to play chess
            quit - playing chess
            help - with possible commands""";
    }
}
