package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turnColor;
    private Map<TeamColor, Integer> pieceCounter;
    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
        pieceCounter = new HashMap<>();
        pieceCounter.put(TeamColor.WHITE, 16);
        pieceCounter.put(TeamColor.BLACK, 16);

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return turnColor; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { turnColor = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece != null) {
            Collection<ChessMove> moves = piece.pieceMoves(this.board, piece.getPosition());
            this.setTeamTurn(piece.getTeamColor());
            moves.removeIf(move -> !isValidMove(move));
            return moves;
        }
        else return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (isValidMove(move)) {
            ChessPiece removed = this.board.getPiece(move.getEndPosition());
            if (removed != null) this.pieceCounter.put(removed.getTeamColor(), this.pieceCounter.get(removed.getTeamColor()) -1);
            this.board.movePiece(move);
        }
        else throw new InvalidMoveException();
        this.setTeamTurn(this.turnColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private boolean isValidMove(ChessMove move) {
        ChessPiece movePiece = this.board.getPiece(move.getStartPosition());
        if (movePiece == null) return false;
        else if (movePiece.getTeamColor() != this.turnColor) return false;
        else if (!movePiece.pieceMoves(this.board, movePiece.getPosition()).contains(move)) return false;
        boolean valid = true;
        this.board.movePiece(move);
        if (isInCheck(this.turnColor)) valid = false;
        this.board.undoMove(move);
        movePiece.setPosition(move.getStartPosition());
        return valid;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece king = this.board.getKing(teamColor);
        if (king == null) return false;
        else return isCheck(king);
    }
    private ChessPosition getKingPosition(TeamColor color) {
        ChessPiece king = this.board.getKing(color);
        return king != null ? king.getPosition() : null;
    }
    private boolean isCheck(ChessPiece king) {
        TeamColor enemyColor = king.getTeamColor() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPiece piece;
//        ChessPosition cur;
        Collection<ChessMove> moves;
        for (int row = 1; row < 9; ++row) {
            for (int col = 1; col < 9; ++ col) {
//                cur = new ChessPosition(row, col);
                piece = this.board.getPiece(row, col);
                if (piece != null) {
                    if (piece.getTeamColor() == enemyColor) {
                        moves = piece.pieceMoves(this.board);
                        for (ChessMove move : moves) {
                            if (Objects.equals(move.getEndPosition(), king.getPosition())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPiece king = this.board.getKing(teamColor);
        if (king == null) return false;
        boolean isMate = isCheck(king);
        Collection<ChessMove> moves = this.board.getKing(teamColor).pieceMoves(this.board);
        for (ChessMove move : moves) {
            if (!isMate) break;
            isMate = !isValidMove(move);
        }
        return isMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPiece king = this.board.getKing(teamColor);
        if (king == null) return false;
        boolean isStalemate = !isCheck(king);
        for (ChessMove move : king.pieceMoves(this.board)) {
            if (!isStalemate) break;
            this.board.movePiece(move);
            isStalemate = isCheck(king);
            this.board.undoMove(move);
        }
        return isStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board; }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return this.board; }
}
