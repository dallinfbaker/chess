package server;

import DataAccess.GameDAO;
import DataAccess.GameData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.WebSocket.ResponseException;
import service.GameService;

import java.util.*;

public class GameHandler {
    private final GameService gameService;
    public GameHandler(GameDAO gameDAO) {
        gameService = new GameService(gameDAO);
    }

    public Map<String, Integer> createGame(String gameName) throws ResponseException {
        int id = gameService.createGame(gameName);
        Map<String, Integer> gameId = new HashMap<>();
        gameId.put("gameID", id);
        return gameId;
    }
    public GameData joinGame(String body, String username) throws ResponseException {
        JsonObject jsonObject = new Gson().fromJson(body, JsonObject.class);
        try {
            int gameID = jsonObject.get("gameID").getAsInt();
            try {
                String playerColor = jsonObject.get("playerColor").getAsString();
                gameService.joinGame(gameID, playerColor, username);
            } catch (NullPointerException e) { return gameService.watchGame(gameID); }
        } catch (NullPointerException e) {
            throw new ResponseException(400, "Error: bad request");
        }

        String playerColor = jsonObject.get("playerColor").getAsString();
        int gameID = jsonObject.get("gameID").getAsInt();
        gameService.joinGame(gameID, playerColor, username);
        return null;
    }

    public HashMap<Integer, GameData> listGames() throws ResponseException {
        return gameService.listGames();
    }
}
