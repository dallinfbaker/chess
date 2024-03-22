package server.WebSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameDataRecord;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;

@WebSocket
public class WebSocketHandler extends Endpoint {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;

    public WebSocketHandler(AuthService auth, GameService game, UserService  user) {
        authService = auth;
        gameService = game;
        userService = user;
    }

    private void authHandler(String auth) throws ResponseException { authService.checkAuth(auth); }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserCommand command = new Gson().fromJson(message, UserCommand.class);
        String auth = command.getAuth().authToken(), username = command.getAuth().username();
        int id = command.getGameId();
        try {
            switch (command.getType()) {
                case JOIN_PLAYER -> joinGame(auth, username, ((JoinPlayerCommand) command).getColor(), command.getGameId(), session);
                case JOIN_OBSERVER -> observeGame(auth, username, session);
                case LEAVE -> leaveGame(auth, username, ((LeaveCommand) command).getColor(), id);
                case RESIGN -> resignGame(auth, username);
                case MAKE_MOVE -> makeMove(auth, username, ((MakeMoveCommand) command).getMove(), id);
            }
        } catch (ResponseException e) { sendError(e.getMessage()); }
    }

    private void sendNotification(String username, String msg) throws ResponseException { sendBroadcast(username, new NotificationMessage(msg)); }
    private void sendLoadGame(String username, String msg, GameDataRecord gameData) throws ResponseException { sendBroadcast(username, new LoadGameMessage(msg, gameData)); }
    private void sendError(String msg) {
        try { sendBroadcast("", new ErrorMessage(msg)); }
        catch (ResponseException e) {
            try { sendBroadcast("", new ErrorMessage(msg)); }
            catch (ResponseException ignored) {}
        }
    }

    private void sendBroadcast(String username, ServerMessage message) throws ResponseException {
        try { connections.broadcast(username, message); }
        catch (IOException e) { throw new ResponseException(500, "Error: " + e.getMessage()); }
    }

    private void joinGame(String auth, String username, String color, int id, Session session) throws ResponseException {
        authHandler(auth);
        connections.add(username, session);
        gameService.joinGame(id, color, username);
        sendNotification(username, String.format("%s has joined the game as the %s player", username, color));
    }
    private void observeGame(String auth, String username, Session session) throws ResponseException {
        authHandler(auth);
        connections.add(username, session);
        sendNotification(username, String.format("%s is observing the game", username));
    }

    private void leaveGame(String auth, String username, String color, int id) throws ResponseException {
        authHandler(auth);
        gameService.removePlayer(id, color);
        connections.remove(username);
    }

    private void resignGame(String auth, String username) throws ResponseException {
        authHandler(auth);
        sendNotification(username, String.format("%s has forfeited the game", username));
    }

    private void makeMove(String auth, String username, ChessMove move, int id) throws ResponseException {
        authHandler(auth);
        sendLoadGame(username, String.format("%s made a move", username), gameService.makeMove(id, username, move));
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {}
}
