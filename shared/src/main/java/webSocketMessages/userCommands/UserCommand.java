package webSocketMessages.userCommands;

import model.AuthDataRecord;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserCommand {
    protected CommandType type;
    private final AuthDataRecord authToken;
    private final int gameId;

    public UserCommand(AuthDataRecord token, int id) {
        authToken = token;
        gameId = id;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }



    public AuthDataRecord getAuth() { return authToken; }

    public CommandType getType() { return type; }
    public int getGameId() { return  gameId; }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserCommand that))
            return false;
        return getType() == that.getType() && Objects.equals(getAuth(), that.getAuth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getAuth());
    }
}