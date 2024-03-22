package ui.webSocket;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;

public interface ServerMessageHandler {
    void notify(NotificationMessage message);
    void loadGame(LoadGameMessage message);
    void errorHandler(ErrorMessage message);


}