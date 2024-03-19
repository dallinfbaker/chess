package model;

import java.util.Collection;

public record GameListRecord(Collection<GameDataRecord> games) {

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("GameListRecord\ngames\n\n");
        for (GameDataRecord data: games) {
            output.append("gameID=").append(data.gameID()).append("\n");
            output.append(DrawChessBoard.drawBoard(data.game().getBoard()));
        }
//        output.append("}");
        return output.toString();
    }
}
