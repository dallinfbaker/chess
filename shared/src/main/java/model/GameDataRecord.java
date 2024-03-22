package model;

import chess.ChessGame;

public record GameDataRecord(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, ObservingUsers observers) {}

