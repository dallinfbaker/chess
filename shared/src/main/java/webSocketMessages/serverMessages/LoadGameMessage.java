package webSocketMessages.serverMessages;

import model.GameDataRecord;

public class LoadGameMessage extends ServerMessage {
    private final GameDataRecord game;
    public LoadGameMessage(GameDataRecord gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gameData;
    }

    public GameDataRecord getGame() { return game; }
}
