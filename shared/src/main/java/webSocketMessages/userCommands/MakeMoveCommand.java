package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserCommand {
    private final String playerColor;
    private final ChessMove move;
    public MakeMoveCommand(String token, int id, String playerColor, ChessMove move) {
        super(token, id);
        this.playerColor = playerColor;
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }

    public String getPlayerColor() { return playerColor; }
    public ChessMove getMove() { return move; }
}
