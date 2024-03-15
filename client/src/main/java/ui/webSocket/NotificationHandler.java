package ui.webSocket;

import webSocketMessages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}