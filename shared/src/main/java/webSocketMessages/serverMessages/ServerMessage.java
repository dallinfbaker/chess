package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    private final MessageType serverMessageType;
    private String auth;
//    private final String message;

    public enum MessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(MessageType type) { serverMessageType = type; }

    public MessageType getServerMessageType() { return serverMessageType; }
    public void setAuth(String authToke) { auth = authToke; }
    public String getAuth() { return auth; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerMessage that)) return false;
        return getServerMessageType() == that.getServerMessageType() && Objects.equals(auth, that.getAuth());
    }

    @Override
    public int hashCode() { return Objects.hash(getServerMessageType(), auth); }
}