package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    private final MessageType serverMessageType;
    private final String message;

    public enum MessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(MessageType type, String msg) {
        serverMessageType = type;
        message = msg;
    }

    public MessageType getServerMessageType() { return serverMessageType; }
    public String getMessage() { return message; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerMessage that)) return false;
        return getServerMessageType() == that.getServerMessageType() && Objects.equals(message, that.getMessage());
    }

    @Override
    public int hashCode() { return Objects.hash(getServerMessageType(), message); }
}