package server;

import DataAccess.GameData;
import java.util.Collection;
import java.util.HashMap;

public class GameList {
    private final Collection<GameData> games;
    public GameList(HashMap<Integer, GameData> hashMap) {
        games = hashMap.values();
    }
}
