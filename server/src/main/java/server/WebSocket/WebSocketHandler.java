package server.WebSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameDataRecord;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final GameService gameService;

    public WebSocketHandler(AuthService auth, GameService game) {
        authService = auth;
        gameService = game;
    }

    private void authHandler(String auth) throws ResponseException { authService.checkAuth(auth); }


    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        // Handle the error
        System.err.println("Error occurred in WebSocket session:");
        System.err.println(throwable.getMessage());

        if (throwable instanceof ResponseException) {
            connections.sendError(session, new ErrorMessage(throwable.getMessage()));
        }
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserCommand command = new Gson().fromJson(message, UserCommand.class);
        String auth = command.getAuth().authToken(), username = command.getAuth().username();
        int id = command.getGameId();
        try {
            switch (command.getType()) {
                case JOIN_PLAYER -> joinGame(auth, username, new Gson().fromJson(message, JoinPlayerCommand.class).getColor(), id, session);
                case JOIN_OBSERVER -> observeGame(auth, username, id, session);
                case LEAVE -> leaveGame(auth, username, new Gson().fromJson(message, LeaveCommand.class).getColor(), id);
                case RESIGN -> resignGame(auth, username, id);
                case MAKE_MOVE -> makeMove(auth, username, new Gson().fromJson(message, MakeMoveCommand.class).getMove(), id);
            }
        } catch (ResponseException e) { sendError(id, e.getMessage()); }
    }

    private void sendNotification(int id, String ignoredAuth, String msg) throws ResponseException { sendBroadcast(id, ignoredAuth, new NotificationMessage(msg)); }
    private void sendLoadGame(int id, String msg, GameDataRecord gameData) throws ResponseException { sendBroadcast(id, null, new LoadGameMessage(gameData)); }
    private void sendError(int id, String msg) {
        try { sendBroadcast(id, null, new ErrorMessage(msg)); }
        catch (ResponseException e) {
            try { sendBroadcast(id, null, new ErrorMessage(msg)); }
            catch (ResponseException ignored) {}
        }
    }

    private void sendBroadcast(int id, String ignoredAuth, ServerMessage message) throws ResponseException {
        System.out.println("Sending Broadcast");
        try { connections.broadcast(id, ignoredAuth, message); }
        catch (IOException e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }

    private void joinGame(String auth, String username, String color, int id, Session session) throws ResponseException {
        authHandler(auth);
        connections.add(id, auth, session);
        gameService.joinGame(id, color, username);
        sendNotification(id, auth, String.format("%s has joined the game as the %s player", username, color));
        sendLoadGame(id, "load the game", gameService.getGame(id));
    }
    private void observeGame(String auth, String username, int id, Session session) throws ResponseException {
        authHandler(auth);
        connections.add(id, auth, session);
        gameService.watchGame(id, username);
        sendLoadGame(id, "load the game", gameService.getGame(id));
        sendNotification(id, auth, String.format("%s is observing the game", username));
    }

    private void leaveGame(String auth, String username, String color, int id) throws ResponseException {
        authHandler(auth);
        gameService.removePlayer(id, color);
        connections.remove(id, auth);
        sendNotification(id, auth, String.format("%s has left the game", username));
    }

    private void resignGame(String auth, String username, int id) throws ResponseException {
        authHandler(auth);
        sendNotification(id, auth, String.format("%s has forfeited the game", username));
    }

    private void makeMove(String auth, String username, ChessMove move, int id) throws ResponseException {
        authHandler(auth);
        GameDataRecord postMove = gameService.makeMove(id, move);
        sendLoadGame(id, String.format("%s made a move", username), postMove);
    }

}
