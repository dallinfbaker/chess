package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    public NotificationMessage(String msg) {
        super(MessageType.NOTIFICATION, msg);
    }
}
