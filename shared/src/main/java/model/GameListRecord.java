package model;

import java.util.Collection;

public record GameListRecord(Collection<GameDataRecord> games) {

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("GameListRecord\n{games=");
        for (GameDataRecord data: games) { output.append("\n").append(data.toString()); }
        output.append("\n}");
        return output.toString();
    }
}
