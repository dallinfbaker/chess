package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    private final String message;
    public NotificationMessage(String msg) {
        super(MessageType.NOTIFICATION);
        message = msg;
    }
    public String getMessage() { return message; }
}
