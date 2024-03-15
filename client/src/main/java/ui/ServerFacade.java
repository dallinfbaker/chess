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
import java.util.Objects;

public class ServerFacade {
    private final String serverURL;

    ServerFacade(String url, String port) {
        serverURL = url + ":" + port;
    }

    public void clearDatabase() throws ResponseException {
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }
    public AuthDataRecord registerUser(UserDataRecord user) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, null, user, AuthDataRecord.class);
    }
    public AuthDataRecord login(UserDataRecord user) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, null, user, AuthDataRecord.class);
    }
    public void logout(AuthDataRecord auth) throws ResponseException {
        var path = "/session";
        makeRequest("DELETE", path, auth.authToken(), null, null);
    }
    public GameListRecord listGames(AuthDataRecord auth) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, auth.authToken(), null, GameListRecord.class);
    }
    public int createGame(AuthDataRecord auth, GameDataRecord game) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, auth.authToken(), game, int.class);
    }
    public int joinGame(AuthDataRecord auth, JoinGameData game) throws ResponseException {
        var path = "/game";
        return makeRequest("PUT", path, auth.authToken(), game, int.class);
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
        if (!isSuccessful(status)) { throw new ResponseException(status, "failure: " + status); }
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
