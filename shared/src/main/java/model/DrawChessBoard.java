package model;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

//import static java.lang.System.out;
import static model.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static String drawBoard(ChessBoard board) {
//        board.getPiece(0,0);
        StringBuilder output = new StringBuilder();
        output.append(SET_TEXT_COLOR_GREEN + "\n  a  b  c  d  e  f  g  h \n");
        ChessPiece piece;
        boolean light = false;
        for (int row = 0; row < 8; ++row) {
            output.append(SET_TEXT_COLOR_GREEN).append(row + 1);
            light = row % 2 == 0;

            for (int col = 0; col < 8; ++col) {
                output.append(light ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY);                light = !light;
                piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                output.append(getPieceString(piece));
            }
            output.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_GREEN).append(row + 1).append("\n");
        }
        output.append(SET_TEXT_COLOR_GREEN + "  a  b  c  d  e  f  g  h \n\n");

        return output.toString();
    }

    private static String getPieceString(ChessPiece piece) {
        try {
            return switch (piece.getTeamColor()) {
                case WHITE -> getString(piece, WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_PAWN);
                case BLACK -> getString(piece, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_PAWN);
            };
        } catch (Exception e) { return "   "; }
    }

    private static String getString(ChessPiece piece, String king, String queen, String rook, String knight, String bishop, String pawn) {
        return switch (piece.getPieceType()) {
            case KING -> king;
            case QUEEN -> queen;
            case ROOK -> rook;
            case KNIGHT -> knight;
            case BISHOP -> bishop;
            case PAWN -> pawn;
        };
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
