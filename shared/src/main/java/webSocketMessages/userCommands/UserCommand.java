package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserCommand {
    protected CommandType commandType;
    private final String authToken;
    private final Integer gameID;

    public UserCommand(String token, int id) {
        authToken = token;
        gameID = id;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }



    public String getAuth() { return authToken; }

    public CommandType getCommandType() { return commandType; }
    public int getGameID() { return gameID; }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserCommand that))
            return false;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuth(), that.getAuth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuth());
    }
}