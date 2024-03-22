package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class JoinPlayerCommand extends UserCommand {
    private final String color;
    public JoinPlayerCommand(AuthDataRecord token,  int id, String playerColor) {
        super(token, id);
        color = playerColor;
        type = CommandType.JOIN_PLAYER;
    }

    public String getColor() { return color; }
}
