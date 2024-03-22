package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthDataRecord;
import model.GameDataRecord;
import model.GameListRecord;
import model.UserDataRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String url, String port) { serverURL = url + ":" + port; }

    public void clearDatabase() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }
    public AuthDataRecord registerUser(String... params) throws ResponseException {
        UserDataRecord user = new UserDataRecord(params[0], params[1], params[2]);
        return makeRequest("POST", "/user", null, user, AuthDataRecord.class);
    }
    public AuthDataRecord login(String... params) throws ResponseException {
        try {
            UserDataRecord user = new UserDataRecord(params[0], params[1], "");
            return makeRequest("POST", "/session", null, user, AuthDataRecord.class);
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public void logout(AuthDataRecord auth) throws ResponseException {
        try { makeRequest("DELETE", "/session", auth.authToken(), null, null); }
        catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public GameListRecord listGames(AuthDataRecord auth) throws ResponseException {
        try { return makeRequest("GET", "/game", auth.authToken(), null, GameListRecord.class); }
        catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public GameDataRecord createGame(AuthDataRecord auth, String... params) throws ResponseException {
        if (params.length == 0) throw new ResponseException(400, "Error: bad request");
        try {
            String gameName = Arrays.toString(params).replace("[", "").replace("]", "").replace(",", "");
            GameDataRecord game = new GameDataRecord(0, null, null, gameName, null, null);
            makeRequest("POST", "/game", auth.authToken(), game, Map.class);
            return game;
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }
    public String joinGame(AuthDataRecord auth, int gameId, String... params) throws ResponseException {
        try {
            String output, color;
            try {
                color = params[1];
                output = "Joined";
            } catch (IndexOutOfBoundsException e) {
                color = null;
                output = "Observing";
            }
            JoinGameData game = new JoinGameData(gameId, color);
            makeRequest("PUT", "/game", auth.authToken(), game, null);
            return output;
        } catch (Exception e) { throw new ResponseException(500, e.getMessage()); }
    }

    private <T> T makeRequest(String method, String path, String auth, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);

            if (!Objects.isNull(auth)) connection.addRequestProperty("authorization", auth);

            writeBody(request, connection);
            connection.connect();
            throwIfNotSuccessful(connection);
            return readBody(connection, responseClass);
        } catch (Exception ex) { throw new ResponseException(500, ex.getMessage()); }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) { reqBody.write(reqData.getBytes()); }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var message = http.getResponseMessage();
        if (!isSuccessful(status)) { throw new ResponseException(status, "failure: " + status + " - " + message); }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) { response = new Gson().fromJson(reader, responseClass); }
            }
        }
        return response;
    }
    private boolean isSuccessful(int status) { return status / 100 == 2; }
}
