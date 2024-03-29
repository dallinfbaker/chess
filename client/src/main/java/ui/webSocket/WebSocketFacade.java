package ui.webSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthDataRecord;
import model.GameDataRecord;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler msgHandler;
    private String color;


    public WebSocketFacade(String url, ServerMessageHandler msgHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.msgHandler = msgHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler( new MessageHandler.Whole<String> () {
                public void onMessage(String message) { onMessageReceived(message); }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) { throw new ResponseException(500, ex.getMessage()); }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void onMessageReceived(String message) {
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
        switch (msg.getServerMessageType()) {
            case NOTIFICATION -> msgHandler.notify(new Gson().fromJson(message, NotificationMessage.class));
            case LOAD_GAME -> msgHandler.loadGame(new Gson().fromJson(message, LoadGameMessage.class));
            case ERROR -> msgHandler.errorHandler(new Gson().fromJson(message, ErrorMessage.class));
        }
    }

    private void sendMessage(UserCommand msg) throws ResponseException {
        try { session.getBasicRemote().sendText(new Gson().toJson(msg)); }
        catch (IOException ignored) { throw new ResponseException(500, "Error: unable to send message"); }
    }

    public void leaveGame(GameDataRecord data, AuthDataRecord auth) throws ResponseException { sendMessage(new LeaveCommand(auth, data.gameID(), color)); }
    public void makeMove(GameDataRecord data, AuthDataRecord auth, ChessMove move) throws ResponseException { sendMessage(new MakeMoveCommand(auth, data.gameID(), color, move)); }
    public void resignGame(int id, AuthDataRecord auth) throws ResponseException { sendMessage(new ResignCommand(auth, id, color)); }
    public void joinGame(GameDataRecord data, AuthDataRecord auth) throws ResponseException {
        color = Objects.equals(auth.username(), data.whiteUsername()) ? "white" : Objects.equals(auth.username(), data.blackUsername()) ? "black" : null;
        UserCommand msg;
        if (Objects.isNull(color)) msg = new JoinObserverCommand(auth, data.gameID());
        else msg = new JoinPlayerCommand(auth, data.gameID(), color);
        sendMessage(msg);
    }
}
