package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class LeaveCommand extends UserCommand {
    private final String color;
    public LeaveCommand(AuthDataRecord token,  int id, String playerColor) {
        super(token, id);
        color = playerColor;
        type = CommandType.LEAVE;
    }

    public String getColor() { return color; }

}
