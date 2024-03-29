package webSocketMessages.serverMessages;

import model.GameDataRecord;

public class LoadGameMessage extends ServerMessage {
    private final GameDataRecord gameData;
    public LoadGameMessage(GameDataRecord game) {
        super(MessageType.LOAD_GAME);
        gameData = game;
    }

    public GameDataRecord getGameData() { return gameData; }
}
