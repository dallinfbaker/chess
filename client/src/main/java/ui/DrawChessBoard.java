package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

//import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static String drawBoard(ChessBoard board) {
        board.getPiece(0,0);
        StringBuilder output = new StringBuilder();
        output.append(SET_TEXT_COLOR_GREEN + " a  b  c  d  e  f  g  h ");
        ChessPiece piece;
        boolean light = false;
        for (int row = 0; row < 8; ++row) {
            output.append(SET_TEXT_COLOR_GREEN).append(row + 1);

            for (int col = 0; col < 8; ++col) {
                piece = board.getPiece(new ChessPosition(row + 1, col +1));
                output.append(getPieceString(piece));
            }
            output.append(SET_TEXT_COLOR_GREEN).append(row + 1).append("\n");
        }
        output.append(SET_TEXT_COLOR_GREEN + " a  b  c  d  e  f  g  h ");

        return output.toString();
    }

    private static void drawSquare(PrintStream out, int row, int col, ChessPiece piece) {

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

    private static void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, rand.nextBoolean() ? X : O);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }

                setBlack(out);
            }

            out.println();
        }
    }

    private static void drawVerticalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
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
