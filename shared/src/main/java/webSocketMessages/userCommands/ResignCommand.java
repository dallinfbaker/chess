package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class ResignCommand extends UserCommand {
    private final String color;
    public ResignCommand(AuthDataRecord token,  int id, String playerColor) {
        super(token, id);
        color = playerColor;
        type = CommandType.RESIGN;
    }

    public String getColor() { return color; }
}
