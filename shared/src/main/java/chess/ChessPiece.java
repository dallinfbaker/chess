package chess;

import chess.PieceMovesCalculator.PieceMovesCalculator;
import chess.PieceMovesCalculator.PieceMovesCalculator.*;


import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    private ChessPosition position;
    private ChessBoard board;
    private Collection<ChessMove> moves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        this.moves = null;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    private void addMove(ChessPosition position) {
        ChessMove move = new ChessMove(this.position, position, null);
        this.moves.add(move);
    }
    private void addMove(int row, int col) {
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove move = new ChessMove(this.position, position, null);
        this.moves.add(move);
    }
    private boolean CheckPosition(int row, int column) {
        if (row > 8 || row < 1 || column > 8 || column < 1) { return true; }
        ChessPosition cur = new ChessPosition(row, column);
        if (this.board.getPiece(cur) == null) {
            this.addMove(cur);
        }
        else if (this.board.getPiece(cur).getTeamColor() != this.color) {
            this.addMove(cur);
            return true;
        }
        else {
            return true;
        }
        return false;
    }

    /**
     * Calculate all of the positions a chess piece can move to
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves() {
        throw new RuntimeException("Not implemented");
    }
    private void queenMoves() {
        this.bishopMoves();
        this.rookMoves();
    }
    private void bishopMoves() {
        boolean north = false, south = false, east = false, west = false;
        for (int i = 1; i < 9; ++i) {
            if(!north) north = CheckPosition(this.position.getRow() + i, this.position.getColumn() + i);
            if(!south) south = CheckPosition(this.position.getRow() + i, this.position.getColumn() - i);
            if(!east) east = CheckPosition(this.position.getRow() - i, this.position.getColumn() + i);
            if(!west) west = CheckPosition(this.position.getRow() - i, this.position.getColumn() - i);
        }
    }
    private Collection<ChessMove> knightMoves() {
        throw new RuntimeException("Not implemented");
    }
    private void rookMoves() {
        for (int i = this.position.getRow() + 1; i < 9; ++i) {
            if (CheckPosition(i, this.position.getColumn())) break;
        }
        for (int i = this.position.getRow() - 1; i > 0; --i) {
            if (CheckPosition(i, this.position.getColumn())) break;
        }
        for (int i = this.position.getColumn() + 1; i < 9; ++i) {
             if (CheckPosition(this.position.getRow(), i)) break;
        }
        for (int i = this.position.getColumn() - 1; i > 0; --i) {
            if (CheckPosition(this.position.getRow(), i)) break;
        }
    }
    private void pawnMoves() {
        int direction = this.color == ChessGame.TeamColor.WHITE ? 1 : -1;
        int endRow = this.color == ChessGame.TeamColor.WHITE ? 8 : 1;
        int StartRow = this.color == ChessGame.TeamColor.WHITE ? 2 : 7;

        if (this.position.getRow() == StartRow) {
            CheckPosition(this.position.getRow() + 2, this.position.getColumn());
        }
        if (this.position.getRow() < endRow) {
            ChessPosition cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn());
            if (this.board.getPiece(cur) == null) {
                this.addMove(cur);
                cur = new ChessPosition(this.position.getRow() + direction * 2, this.position.getColumn());
                if (this.position.getRow() == StartRow && board.getPiece(cur) == null) {
                    this.addMove(cur);
                }
            }
            cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() + 1);
            if (this.board.getPiece(cur) != null) {
                if (this.board.getPiece(cur).getTeamColor() != this.color) { this.addMove(cur); }
            }
            cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() - 1);
            if (this.board.getPiece(cur) != null) {
                if (this.board.getPiece(cur).getTeamColor() != this.color) { this.addMove(cur); }
            }
        }
    }

    /**
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.position = myPosition;
//        PieceMovesCalculator calculator;
        this.moves = new ArrayList<ChessMove>();
        switch(this.type) {
            case KING: kingMoves();
            case QUEEN: queenMoves();
            case BISHOP: bishopMoves();
            case KNIGHT: knightMoves();
            case ROOK: rookMoves();
            case PAWN: pawnMoves();
        }
        return this.moves;
    }

    /**
     * @return if 2 pieces are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessPiece.class) { return false; }
        ChessPiece piece = (ChessPiece) o;
        return this.color == piece.color & this.type == piece.type;
    }
/*    *//**
     * @return Hash of position
     *//*
    @Override
    public int hashCode() {
        throw new RuntimeException("Not implemented");
    }*/
}
