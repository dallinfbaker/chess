package server.WebSocket;

import chess.ChessGame;
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
import java.util.Objects;

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
        System.err.println("Error occurred in WebSocket session:");
        System.err.println(throwable.getMessage());
        if (!Objects.isNull(session) && throwable instanceof ResponseException) {
            connections.sendError(session, new ErrorMessage("Error: " + throwable.getMessage())); }
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserCommand command = new Gson().fromJson(message, UserCommand.class);
        String auth = command.getAuth();
        int id = command.getGameID();
//        try { authHandler(auth); }
//        catch (ResponseException e) { connections.sendError(session, new ErrorMessage("Error: bad authentication")); }
        try {
            authHandler(auth);
            String username = authService.getUsername(auth);
//            if (Objects.isNull(username)) throw new ResponseException(400, "null user");
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> joinGame(auth, username, new Gson().fromJson(message, JoinPlayerCommand.class).getPlayerColor(), id, session);
                case JOIN_OBSERVER -> observeGame(auth, username, id, session);
                case LEAVE -> leaveGame(auth, username, id);
                case RESIGN -> resignGame(auth, username, id);
                case MAKE_MOVE -> makeMove(auth, username, new Gson().fromJson(message, MakeMoveCommand.class).getMove(), id);
            }
        } catch (ResponseException e)
        { connections.sendError(session, new ErrorMessage("Error: " + e.getMessage())); }
//        { sendError(id, e.getMessage()); }
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

    private void joinGame(String auth, String username, ChessGame.TeamColor color, int id, Session session) throws ResponseException {
//        authHandler(auth);
//        if (Objects.isNull(username))
        connections.add(id, auth, session);
        String name = Objects.equals(color, ChessGame.TeamColor.WHITE) ? gameService.getGame(id).whiteUsername() : gameService.getGame(id).blackUsername();
        if (Objects.isNull(name)) throw new ResponseException(401, "user not in game");
        gameService.joinGame(id, color.toString(), username);
        sendNotification(id, auth, String.format("%s has joined the game as the %s player", username, color));
//        sendLoadGame(id, "load the game", gameService.getGame(id));
        connections.sendSingleMessage(id, auth, new LoadGameMessage(gameService.getGame(id)));
    }
    private void observeGame(String auth, String username, int id, Session session) throws ResponseException {
//        authHandler(auth);
        connections.add(id, auth, session);
        gameService.watchGame(id, username);
        sendNotification(id, auth, String.format("%s is observing the game", username));
//        sendLoadGame(id, "load the game", gameService.getGame(id));
        connections.sendSingleMessage(id, auth, new LoadGameMessage(gameService.getGame(id)));
    }

    private void leaveGame(String auth, String username, int id) throws ResponseException {
//        authHandler(auth);
        gameService.removePlayer(id, username);
        connections.remove(id, auth);
        sendNotification(id, auth, String.format("%s has left the game", username));
    }

    private void resignGame(String auth, String username, int id) throws ResponseException {
        authHandler(auth);
        gameService.resignGame(id, username);
//        GameDataRecord game = gameService.getGame(id);
//        ChessGame.TeamColor playerColor = Objects.equals(username, game.whiteUsername()) ? ChessGame.TeamColor.WHITE : Objects.equals(username, game.blackUsername()) ? ChessGame.TeamColor.BLACK : null;
//        if(Objects.isNull(playerColor)) throw new ResponseException(401, "not a player");
//        game.game().resign(playerColor);
//        gameService.
        sendNotification(id, null, String.format("%s has forfeited the game", username));
    }

    private void makeMove(String auth, String username, ChessMove move, int id) throws ResponseException {
//        authHandler(auth);
//        GameDataRecord game = gameService.getGame(id);
//        ChessGame.TeamColor playerColor = Objects.equals(username, game.whiteUsername()) ? ChessGame.TeamColor.WHITE : Objects.equals(username, game.blackUsername()) ? ChessGame.TeamColor.BLACK : null;
//        if(Objects.isNull(playerColor)) throw new ResponseException(401, "not a player");
////        gameService.getGame(id).whiteUsername();
//        ChessGame.TeamColor moveColor = game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor();
//
//        if (!Objects.equals(playerColor, moveColor)) throw new ResponseException(401, "cannot move opponent's piece");

        GameDataRecord postMove = gameService.makeMove(id, move, username);
        sendNotification(id, auth, String.format("%s made a move", username));
        sendLoadGame(id, String.format("%s made a move", username), postMove);
//        try {
//            GameDataRecord postMove = gameService.makeMove(id, move);
//            sendLoadGame(id, String.format("%s made a move", username), postMove);
//        } catch (ResponseException e) {
//
//        }

    }

}
