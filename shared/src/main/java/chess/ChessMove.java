package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition start;
    ChessPosition end;
    ChessPiece.PieceType promotion;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        start = startPosition;
        end = endPosition;
        if (this.end.getRow() == 7) { promotion = promotionPiece; }
        else { promotion = null; }
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() { return this.promotion; }
}
