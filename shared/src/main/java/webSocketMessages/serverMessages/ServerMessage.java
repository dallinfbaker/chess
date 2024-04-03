package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    private final ServerMessageType serverMessageType;
    private String authToken;
//    private final String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) { serverMessageType = type; }

    public ServerMessageType getServerMessageType() { return serverMessageType; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }
    public String getAuthToken() { return authToken; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerMessage that)) return false;
        return getServerMessageType() == that.getServerMessageType() && Objects.equals(authToken, that.getAuthToken());
    }

    @Override
    public int hashCode() { return Objects.hash(getServerMessageType(), authToken); }
}