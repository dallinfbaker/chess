package webSocketMessages.userCommands;

import chess.ChessMove;
import model.AuthDataRecord;

public class MakeMoveCommand extends UserCommand {
    private final String color;
    private final ChessMove move;
    public MakeMoveCommand(AuthDataRecord token,  int id, String playerColor, ChessMove move) {
        super(token, id);
        color = playerColor;
        this.move = move;
        type = CommandType.MAKE_MOVE;
    }

    public String getColor() { return color; }
    public ChessMove getMove() { return move; }
}
