package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private final String message;
    public ErrorMessage(String msg) {
        super(MessageType.ERROR);
        message = msg;
    }
    public String getMessage() { return message; }
}
