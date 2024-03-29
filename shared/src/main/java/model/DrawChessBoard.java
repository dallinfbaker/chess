package model;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

import static model.EscapeSequences.*;

public class DrawChessBoard {

    public static String drawBoard(ChessBoard board, boolean reverse) { return drawBoard(board, reverse, new HashSet<>()); }
    public static String drawBoard(ChessBoard board, boolean reverse, Collection<ChessMove> moves) {
        Collection<ChessPosition> endings = new HashSet<>();
        if (!Objects.isNull(moves)) for (ChessMove move : moves) {
            endings.add(move.getStartPosition());
            endings.add(move.getEndPosition());
        }
        StringBuilder output = new StringBuilder("\n");
        output.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_GREEN);
        addHeader(output, reverse);
        ChessPiece piece;
        ChessPosition position;
        boolean light = true;
        int direction = reverse ? -1 : 1, end = reverse ? -1 : 8, start = reverse ? 7 : 0;
        for (int row = start; reverse ? row > end : row < end; row = row + direction) {
            output.append(SET_TEXT_COLOR_GREEN).append(row + 1).append(" ");
            for (int col = end - direction; reverse ? col < start - direction : col > start - direction; col = col - direction) {
                position = new ChessPosition(row + 1, col + 1);
                setBackground(output, position, endings, light);
                light = !light;
                piece = board.getPiece(position);
                addPieceString(piece, output);
            }
            output.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_GREEN).append(" ").append(row + 1).append("\n");
            light = !light;
        }
        addHeader(output, reverse);
        output.append("\n").append(SET_TEXT_COLOR_WHITE);
        return output.toString();
    }

    private static void addHeader(StringBuilder output, boolean reverse) {
        List<String> headers = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        if (!reverse) headers = headers.reversed();
        output.append("  ").append(SET_SPACING);
        for (String c : headers) { output.append(c).append(" ").append(SET_SPACING); }
        output.append("\n");
    }

    private static void addPieceString(ChessPiece piece, StringBuilder output) {
        try {
            switch (piece.getTeamColor()) {
                case WHITE -> {
                    setTextWhite(output);
                    output.append(getString(piece, WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_PAWN));
                }
                case BLACK -> {
                    setTextBlack(output);
                    output.append(getString(piece, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_PAWN));
                }
            }
        } catch (Exception e) { output.append("  ").append(SET_SPACING); }
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

    private static void setBackground(StringBuilder output, ChessPosition current, Collection<ChessPosition> endings, boolean light) {
//        if (endings.contains(current)) output.append(light ? SET_BG_COLOR_LIGHT_GREEN : SET_BG_COLOR_DARK_GREEN);
        if (endings.contains(current)) output.append(light ? SET_BG_COLOR_LIGHT_BLUE : SET_BG_COLOR_DARK_BLUE);
        else output.append(light ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY);
    }

    private static void setTextWhite(StringBuilder output) { output.append(SET_TEXT_COLOR_WHITE); }
    private static void setTextBlack(StringBuilder output) { output.append(SET_TEXT_COLOR_BLACK); }

}
