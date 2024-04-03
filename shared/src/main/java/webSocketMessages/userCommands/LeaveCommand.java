package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class LeaveCommand extends UserCommand {
//    private final String color;
    public LeaveCommand(String token,  int id/*, String playerColor*/) {
        super(token, id);
//        color = playerColor;
        commandType = CommandType.LEAVE;
    }

//    public String getColor() { return color; }

}
