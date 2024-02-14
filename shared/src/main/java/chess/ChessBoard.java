package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board;
    private ChessPiece whiteKing;
    private ChessPiece blackKing;
    private ChessPiece removedPiece;
    public ChessBoard() { board = new ChessPiece[8][8]; }

    public ChessPiece getKing(ChessGame.TeamColor color) {
        return color == ChessGame.TeamColor.WHITE ? whiteKing : blackKing;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()-1][position.getColumn()-1] = piece;
        if (piece != null) {
            piece.setPosition(position);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) whiteKing = piece;
                else blackKing = piece;
            }
        }
    }

    /**
     * move a chess piece on the chessboard
     */
    public void movePiece(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition(), endPosition = move.getEndPosition();
        ChessPiece piece = this.getPiece(startPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.KING && Math.abs(startPosition.getColumn() - endPosition.getColumn()) == 2) {
            moveCastle(startPosition, endPosition, piece);
        }
        else {
            piece.setPieceType(move.getPromotionPiece());
            this.removedPiece = this.board[endPosition.getRow() - 1][endPosition.getColumn() - 1];
            this.board[endPosition.getRow() - 1][endPosition.getColumn() - 1] = piece;
            this.board[startPosition.getRow() - 1][startPosition.getColumn() - 1] = null;
            piece.setPosition(endPosition);
            piece.increaseMoveCount();
            if (move.getEnPassant()) {
                this.removedPiece = move.getOther();
                this.board[removedPiece.getPosition().getRow() - 1][removedPiece.getPosition().getColumn() - 1] = null;
            }
        }
    }
    public void undoMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition(), endPosition = move.getEndPosition();
        ChessPiece piece = this.getPiece(endPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.KING && Math.abs(startPosition.getColumn() - endPosition.getColumn()) == 2) {
            undoMoveCastle(startPosition, endPosition, piece);
        }
        else {
            if (move.getPromotionPiece() != null) piece.setPieceType(ChessPiece.PieceType.PAWN);
            this.board[startPosition.getRow() - 1][startPosition.getColumn() - 1] = piece;
            this.board[endPosition.getRow() - 1][endPosition.getColumn() - 1] = this.removedPiece;
            piece.setPosition(startPosition);
            this.removedPiece = null;
            piece.decreaseMoveCount();
        }
    }
    private void moveCastle(ChessPosition kingStart, ChessPosition kingEnd, ChessPiece king) {
        int row = kingStart.getRow();
        ChessPiece rook;
        ChessPosition rookStart, rookEnd;
        if (kingEnd.getColumn() < 5) {
            rook = board[row - 1][0];
            rookStart = new ChessPosition(row, 1);
            rookEnd = new ChessPosition(row, 4);
        }
        else {
            rook = board[row - 1][7];
            rookStart = new ChessPosition(row, 8);
            rookEnd = new ChessPosition(row, 6);
        }
        this.board[kingStart.getRow()-1][kingStart.getColumn()-1] = null;
        this.board[rookStart.getRow()-1][rookStart.getColumn()-1] = null;
        this.board[kingEnd.getRow()-1][kingEnd.getColumn()-1] = king;
        this.board[rookEnd.getRow()-1][rookEnd.getColumn()-1] = rook;
        king.increaseMoveCount();
        rook.increaseMoveCount();
    }
    private void undoMoveCastle(ChessPosition kingStart, ChessPosition kingEnd, ChessPiece king) {
        int row = kingStart.getRow();
        ChessPiece rook;
        ChessPosition rookStart, rookEnd;
        if (kingEnd.getColumn() < 5) {
            rook = board[row - 1][3];
            rookStart = new ChessPosition(row, 1);
            rookEnd = new ChessPosition(row, 4);
        }
        else {
            rook = board[row - 1][5];
            rookStart = new ChessPosition(row, 8);
            rookEnd = new ChessPosition(row, 6);
        }
        this.board[kingEnd.getRow()-1][kingEnd.getColumn()-1] = null;
        this.board[rookEnd.getRow()-1][rookEnd.getColumn()-1] = null;
        this.board[kingStart.getRow()-1][kingStart.getColumn()-1] = king;
        this.board[rookStart.getRow()-1][rookStart.getColumn()-1] = rook;

        king.decreaseMoveCount();
        rook.decreaseMoveCount();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) { return this.board[position.getRow()-1][position.getColumn()-1]; }
    public ChessPiece getPiece(int row, int col) { return this.board[row - 1][col - 1]; }

        /**
         * Returns the proper piece for the location on the board
         * */
    private ChessPiece.PieceType getStartingPiece(int row, int col) {
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
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        ChessPiece.PieceType type;
        ChessPiece piece;
        ChessPosition pos;
        for (int i = 1; i < 9; ++i) {
            if (i == 3) { color = ChessGame.TeamColor.BLACK;}
            for (int j = 1; j < 9; ++j) {
                type = getStartingPiece(i,j);
                if (type != null) { piece = new ChessPiece(color, type); }
                else { piece = null; }
                pos = new ChessPosition(i,j);
                addPiece(pos,piece);
            }
        }
    }

    public ChessBoard copyBoard() {
        ChessBoard copy = new ChessBoard();
        ChessPosition cur;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                cur = new ChessPosition(i,j);
                copy.addPiece(cur, this.getPiece(cur));
            }
        }
        return copy;
    }

    /**
     * @return if 2 boards are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessBoard.class) { return false; }
        ChessBoard board = (ChessBoard) o;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (!Objects.equals(this.board[i][j], board.board[i][j])) { return false; }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                ", whiteKing=" + whiteKing +
                ", blackKing=" + blackKing +
                ", removedPiece=" + removedPiece +
                '}';
    }
}
