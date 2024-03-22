package ui.webSocket;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void notify(NotificationMessage message);
    void loadGame(LoadGameMessage message);
    void errorHandler(ErrorMessage message);


}