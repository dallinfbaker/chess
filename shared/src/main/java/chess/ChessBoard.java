package chess;

import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets every element in the board to null
     * */
    private void clearBoard() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.board[i][j] = null;
            }
        }
    }

    /**
     * Returns the proper piece for the location on the board
     * */
    private ChessPiece.PieceType getStartingPiece(int row, int col, ChessGame.TeamColor color) {
        switch(row) {
            case 1,8:
                switch (col) {
                    case 1, 8:
                        return ChessPiece.PieceType.ROOK;
                    case 2, 7:
                        return ChessPiece.PieceType.KNIGHT;
                    case 3, 6:
                        return ChessPiece.PieceType.BISHOP;
                    case 4:
                        return ChessPiece.PieceType.QUEEN;
                    case 5:
                        return ChessPiece.PieceType.KING;
                }
            case 2,7:
                return ChessPiece.PieceType.PAWN;
            default:
                return null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
//        this.clearBoard();
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        ChessPiece.PieceType type;
        ChessPiece piece;
        ChessPosition pos;
        for (int i = 1; i < 9; ++i) {
            if (i == 3) { color = ChessGame.TeamColor.BLACK;}
            for (int j = 1; j < 9; ++j) {
                type = getStartingPiece(i,j,color);
                if (type != null) { piece = new ChessPiece(color, type); }
                else { piece = null; }
                pos = new ChessPosition(i,j);
                addPiece(pos,piece);
            }
        }
    }

    /**
     * Compares 2 boards
     * @return if boards are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessBoard.class) { return false; }
        ChessBoard board = (ChessBoard) o;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (Objects.equals(this.board[i][j], board.board[i][j])) { continue; }
                else { return false;}
            }
        }
        return true;
    }

}
