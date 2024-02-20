package server;

import DataAccess.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameList {
//    { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
    private Collection<GameData> games;

    public GameList(HashMap<Integer, GameData> hashMap) {
        games = hashMap.values();
    }
}
