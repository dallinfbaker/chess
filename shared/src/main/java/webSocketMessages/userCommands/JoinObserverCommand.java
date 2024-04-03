package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserCommand {
    public JoinObserverCommand(String token, int id) {
        super(token, id);
        commandType = CommandType.JOIN_OBSERVER;
    }
}
