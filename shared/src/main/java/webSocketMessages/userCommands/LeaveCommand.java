package webSocketMessages.userCommands;

public class LeaveCommand extends UserCommand {
    public LeaveCommand(String token,  int id) {
        super(token, id);
        commandType = CommandType.LEAVE;
    }
}
