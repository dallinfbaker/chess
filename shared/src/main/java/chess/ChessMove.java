package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotion;
    private boolean enPassant = false;
//    private boolean castle;
    private  ChessPiece other = null;
//    private ChessPiece king = null;
//    private ChessPosition otherStart = null;
//    private ChessPosition otherEnd = null;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        promotion = promotionPiece;
    }
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, boolean enPassant, ChessPiece other) {
        start = startPosition;
        end = endPosition;
        promotion = null;
        this.enPassant = enPassant;
        this.other = other;
    }
//    public ChessMove(ChessPosition rookEnd, ChessPosition kingEnd, boolean castle, ChessPiece king, ChessPiece rook) {
//        this.other = rook;
//        this.king = king;
//        start = king.getPosition();
//        end = kingEnd;
//        otherStart = rook.getPosition();
//        otherEnd = rookEnd;
//        promotion = null;
//        this.castle = castle;
//    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() { return this.start; }
    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }
//    public ChessPosition getOtherStartPosition() { return this.otherStart; }
    /**
     * @return ChessPosition of ending location
     */
//    public ChessPosition getOtherEndPosition() { return this.otherEnd; }
    public boolean getEnPassant() { return this.enPassant; }

    public ChessPiece getOther() { return this.other; }
//    public ChessPiece getKing() { return this.king; }
//    public boolean getCastle() { return this.castle; }


    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() { return this.promotion; }

    /**
     * @return if 2 moves are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessMove.class) { return false; }
        ChessMove move = (ChessMove) o;
        return (Objects.equals(this.start, move.getStartPosition()) && Objects.equals(this.end, move.getEndPosition()) && this.promotion == move.getPromotionPiece());
    }

    /**
     * @return Hash of position
     */
    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotion);
    }
}
