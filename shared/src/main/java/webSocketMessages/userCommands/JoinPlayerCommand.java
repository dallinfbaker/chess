package webSocketMessages.userCommands;

import chess.ChessGame;
import model.AuthDataRecord;

public class JoinPlayerCommand extends UserCommand {
    private final ChessGame.TeamColor playerColor;
    public JoinPlayerCommand(String token, int id, String playerColor) {
        super(token, id);
        this.playerColor = playerColor.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        commandType = CommandType.JOIN_PLAYER;
    }
    public ChessGame.TeamColor getPlayerColor() { return playerColor; }
}
