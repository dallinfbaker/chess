package server;

import DataAccess.GameData;
import java.util.Collection;

public record GameListRecord(Collection<GameData> games) {}
