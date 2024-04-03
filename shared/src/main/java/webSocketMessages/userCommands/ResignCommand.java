package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class ResignCommand extends UserCommand {
    private final String playerColor;
    public ResignCommand(String token, int id, String playerColor) {
        super(token, id);
        this.playerColor = playerColor;
        commandType = CommandType.RESIGN;
    }

    public String getPlayerColor() { return playerColor; }
}
