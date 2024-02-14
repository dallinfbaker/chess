package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    private ChessPosition position;
    private ChessBoard board;
    private Collection<ChessMove> moves;
    private int moveCount;
    private boolean enPassantVulnerable;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        this.moveCount = 0;
        this.moves = null;
        this.enPassantVulnerable = false;
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

    public void setPosition(ChessPosition position) {
        this.position = position;
    }
    public ChessPosition getPosition() {
        return this.position;
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
    public void setPieceType(PieceType type) { if (type != null) this.type = type; }
    private void addMove(ChessPosition position) {
        ChessMove move = new ChessMove(this.position, position, null);
        this.moves.add(move);
    }

    /**
     * add move of the correct type
     */
    private void addMove(ChessPosition position, PieceType promotion) {
        ChessMove move = new ChessMove(this.position, position, promotion);
        this.moves.add(move);
    }
    private void addMoveEnPassant(ChessPosition position, ChessPiece other) {
        ChessMove move = new ChessMove(this.position, position, true, other);
        this.moves.add(move);
    }
    private void addMoveCastle(ChessPiece king, ChessPiece rook) {
        ChessPosition start = king.getPosition();
        ChessPosition end = rook.getPosition().getColumn() == 1 ?
                new ChessPosition(start.getRow(), 3) :new ChessPosition(start.getRow(), 7);
        ChessMove move = new ChessMove(start, end, null);
        this.moves.add(move);
    }
    private boolean checkPosition(int row, int column) {
        if ((row > 8 || row < 1) || (column > 8 || column < 1)) { return true; }
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
     * Calculate all positions a chess piece can move to
     */
    private void kingMoves() {
        checkPosition(this.position.getRow() + 1, this.position.getColumn() + 1);
        checkPosition(this.position.getRow() + 1, this.position.getColumn());
        checkPosition(this.position.getRow() + 1, this.position.getColumn() - 1);
        checkPosition(this.position.getRow(), this.position.getColumn() + 1);
        checkPosition(this.position.getRow(), this.position.getColumn() - 1);
        checkPosition(this.position.getRow() - 1, this.position.getColumn() + 1);
        checkPosition(this.position.getRow() - 1, this.position.getColumn());
        checkPosition(this.position.getRow() - 1, this.position.getColumn() - 1);
        this.castleMove();
    }
    private ChessPiece spaceToPiece(int colDir) {
        ChessPosition cur = new ChessPosition(this.position.getRow(), this.position.getColumn());
        while (cur.getColumn() > 1 && cur.getColumn() < 8) {
            if (this.board.getPiece(cur) == null || this.board.getPiece(cur) == this) {
                cur = new ChessPosition(cur.getRow(), cur.getColumn() + colDir);
            }
            else break;
        }
        return this.board.getPiece(cur);
    }
    private void castleMove() {
        if (this.moveCount == 0) {
            if (this.type == PieceType.KING) {
                ChessPiece rook1 = spaceToPiece(1);
                ChessPiece rook2 = spaceToPiece(-1);
                if (rook1 != null) {
                    if (rook1.getMoveCount() == 0 && rook1.getPieceType() == PieceType.ROOK) {
                        this.addMoveCastle(this, rook1);
                    }
                }
                if (rook2 != null) {
                    if (rook2.getMoveCount() == 0 && rook2.getPieceType() == PieceType.ROOK) {
                        this.addMoveCastle(this, rook2);
                    }
                }
            }
        }
    }
    private void queenMoves() {
        this.bishopMoves();
        this.rookMoves();
    }
    private void bishopMoves() {
        boolean north = false, south = false, east = false, west = false;
        for (int i = 1; i < 9; ++i) {
            if (!north) north = checkPosition(this.position.getRow() + i, this.position.getColumn() + i);
            if (!south) south = checkPosition(this.position.getRow() + i, this.position.getColumn() - i);
            if (!east) east = checkPosition(this.position.getRow() - i, this.position.getColumn() + i);
            if (!west) west = checkPosition(this.position.getRow() - i, this.position.getColumn() - i);
        }
    }
    private void knightMoves() {
        int i = 1, j = 2;
        KnightSet(j, i);
        KnightSet(i, j);
    }
    private void KnightSet(int i, int j) {
        checkPosition(this.position.getRow() + j, this.position.getColumn() + i);
        checkPosition(this.position.getRow() + j, this.position.getColumn() - i);
        checkPosition(this.position.getRow() - j, this.position.getColumn() + i);
        checkPosition(this.position.getRow() - j, this.position.getColumn() - i);
    }
    private void rookMoves() {
        for (int i = this.position.getRow() + 1; i < 9; ++i) {
            if (checkPosition(i, this.position.getColumn())) break;
        }
        for (int i = this.position.getRow() - 1; i > 0; --i) {
            if (checkPosition(i, this.position.getColumn())) break;
        }
        for (int i = this.position.getColumn() + 1; i < 9; ++i) {
             if (checkPosition(this.position.getRow(), i)) break;
        }
        for (int i = this.position.getColumn() - 1; i > 0; --i) {
            if (checkPosition(this.position.getRow(), i)) break;
        }
//        this.castleMove();
    }
    private void pawnMoves() {
        int direction = this.color == ChessGame.TeamColor.WHITE ? 1 : -1;
        int endRow = this.color == ChessGame.TeamColor.WHITE ? 8 : 1;

        if (this.position.getRow() * direction < endRow * direction) {
            this.pawnForward(direction);
            this.pawnCapture(direction);
            if (this.position.getRow() == endRow - direction) pawnPromotions();
        }
    }
    private void pawnPromotions() {
        PieceType[] pieces = new PieceType[]{PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT, PieceType.QUEEN};
        ChessMove[] movesArray = this.moves.toArray(new ChessMove[0]);
        this.moves.clear();
        for (ChessMove move : movesArray) {
            for (PieceType type : pieces) {
                this.addMove(move.getEndPosition(), type);
            }
        }
    }
    private void pawnForward(int direction) {
        int StartRow = this.color == ChessGame.TeamColor.WHITE ? 2 : 7;
        ChessPosition cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn());
        if (this.board.getPiece(cur) == null) {
            this.addMove(cur);
            cur = new ChessPosition(this.position.getRow() + direction * 2, this.position.getColumn());
            if (this.position.getRow() == StartRow && board.getPiece(cur) == null) {
                this.addMove(cur);
                this.enPassantVulnerable = true;
            }
        }
    }
    private void pawnCapture(int direction) {
        ChessPosition cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() + 1);
        if (cur.getColumn() < 9) {
            if (this.board.getPiece(cur) != null) {
                if (this.board.getPiece(cur).getTeamColor() != this.color) {
                    this.addMove(cur);
                }
            }
            this.pawnEnPassant(cur);
        }
        cur = new ChessPosition(this.position.getRow() + direction, this.position.getColumn() - 1);
        if (cur.getColumn() > 0) {
            if (this.board.getPiece(cur) != null) {
                if (this.board.getPiece(cur).getTeamColor() != this.color) {
                    this.addMove(cur);
                }
            }
            this.pawnEnPassant(cur);
        }
    }
    private void pawnEnPassant(ChessPosition cur) {
        int enPassantRow = this.color == ChessGame.TeamColor.WHITE ? 5 : 4;
        if (this.position.getRow() == enPassantRow) {
            ChessPiece other = board.getPiece(this.position.getRow(), cur.getColumn());
            if (other != null) {
                if (other.getPieceType() == PieceType.PAWN && other.getEnPassantVulnerable() && other.getTeamColor() != this.getTeamColor()) {
                    this.addMoveEnPassant(cur, other);
                }
            }
        }
    }


    /**
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) { return pieceMoves(board); }
    public Collection<ChessMove> pieceMoves(ChessBoard board) {
        this.board = board;
        this.moves = new HashSet<>();
        switch(this.type) {
            case KING:
                kingMoves();
                break;
            case QUEEN:
                queenMoves();
                break;
            case BISHOP:
                bishopMoves();
                break;
            case KNIGHT:
                knightMoves();
                break;
            case ROOK:
                rookMoves();
                break;
            case PAWN:
                pawnMoves();
                break;
        }
        return this.moves;
    }

    public boolean getEnPassantVulnerable() { return this.enPassantVulnerable; }
    public void setEnPassantVulnerable(boolean enPassantVulnerable) { this.enPassantVulnerable = enPassantVulnerable; }
    public int getMoveCount() { return this.moveCount; }
    public void increaseMoveCount() { this.moveCount++; }
    public void decreaseMoveCount() { this.moveCount--; }

    /**
     * @return if 2 pieces are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessPiece.class) { return false; }
        ChessPiece piece = (ChessPiece) o;
        return this.color == piece.color & this.type == piece.type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                ", position=" + position +
                ", board=" + board +
                ", moves=" + moves +
                ", moveCount=" + moveCount +
                ", enPassantVulnerable=" + enPassantVulnerable +
                '}';
    }
}
