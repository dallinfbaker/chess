package webSocketMessages.serverMessages;

import model.GameDataRecord;

public class LoadGameMessage extends ServerMessage {
    private final GameDataRecord gameData;
    public LoadGameMessage(String msg, GameDataRecord game) {
        super(MessageType.LOAD_GAME, msg);
        gameData = game;
    }

    public GameDataRecord getGameData() { return gameData; }
}
