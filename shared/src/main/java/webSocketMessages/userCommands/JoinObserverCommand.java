package webSocketMessages.userCommands;

import model.AuthDataRecord;

public class JoinObserverCommand extends UserCommand {
    public JoinObserverCommand(AuthDataRecord token,  int id) {
        super(token, id);
        type = CommandType.JOIN_OBSERVER;
    }
}
