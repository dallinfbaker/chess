package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    public ErrorMessage(String msg) {
        super(MessageType.ERROR, msg);
    }
}
