package chess;

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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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

    /**
     * Calculate all of the positions a chess piece can move to
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves() {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> queenMoves() {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> bishopMoves() {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> knightMoves() {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookMoves() {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> pawnMoves() {
        Collection<ChessMove> moves = null;
        ChessMove move;
        ChessPosition endPosition;
        int direction = this.color == ChessGame.TeamColor.WHITE ? 1 : -1;
        int endRow = this.color == ChessGame.TeamColor.WHITE ? 8 : 1;
        int doubleRow = this.color == ChessGame.TeamColor.WHITE ? 2 : 7;

        if (this.position.getRow() < endRow) {
            if (this.board.getPiece(this.position, direction, 0) == null) {
                endPosition = new ChessPosition(this.position.getRow() + direction, this.position.getColumn());
                move = new ChessMove(this.position, endPosition, PieceType.QUEEN);
                moves.add(move);
                if (this.position.getRow() == doubleRow) {
                    if (this.board.getPiece(this.position, direction * 2, 0) == null) {
                        endPosition = new ChessPosition(this.position.getRow() + direction * 2, this.position.getColumn());
                        move = new ChessMove(this.position, endPosition, PieceType.QUEEN);
                        moves.add(move);
                    }
                }
            }
        }
        if (this.position.getColumn() < 7) {
            if (this.board.getPiece(this.position, direction, 1) != null) {
                endPosition = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() + 1);
                move = new ChessMove(this.position, endPosition, PieceType.QUEEN);
                moves.add(move);
            }
        }
        if (this.position.getColumn() > 0) {
            if (this.board.getPiece(this.position, direction, -1) != null) {
                endPosition = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() - 1);
                move = new ChessMove(this.position, endPosition, PieceType.QUEEN);
                moves.add(move);
            }
        }
        return moves;
    }

    /**
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.position = myPosition;
        Collection<ChessMove> moves = null;
        switch(this.type) {
            case KING: moves = kingMoves();
            case QUEEN: moves = queenMoves();
            case BISHOP: moves = bishopMoves();
            case KNIGHT: moves = knightMoves();
            case ROOK: moves = rookMoves();
            case PAWN: moves = pawnMoves();
        }
        return moves;
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
}
