package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    TeamColor turnColor;
    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
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
            Collection<ChessMove> moves = piece.pieceMoves(this.board, startPosition);
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
        if (isValidMove(move)) this.board.movePiece(move);
        else throw new InvalidMoveException();
        this.setTeamTurn(this.turnColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private boolean isValidMove(ChessMove move) {
        ChessPiece movePiece = this.board.getPiece(move.getStartPosition());
        if (movePiece == null) return false;
        else if (movePiece.getTeamColor() != this.turnColor) return false;
        else if (!movePiece.pieceMoves(this.board, move.getStartPosition()).contains(move)) return false;
        ChessBoard tempBoard = this.board.copyBoard();
        boolean valid = true;
        this.board.movePiece(move);
        if (isInCheck(this.turnColor)) valid = false;
        this.board = tempBoard;
        return valid;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        if (kingPosition == null) return false;
        return isCheckPosition(kingPosition, teamColor);
    }
    private ChessPosition getKingPosition(TeamColor color) {
        ChessPiece king;
        ChessPosition cur;
        for (int row = 1; row < 9; ++row) {
            for (int col = 1; col < 9; ++ col) {
                cur = new ChessPosition(row, col);
                king = this.board.getPiece(cur);
                if (king != null) {
                    if (king.getPieceType() == ChessPiece.PieceType.KING && king.getTeamColor() == color) return cur;
                }
            }
        }
        return null;
//        throw new RuntimeException(String.format("No %s King on board",color.name()));
    }
    private boolean isCheckPosition(ChessPosition pos, TeamColor color) {
        TeamColor enemyColor = color == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPiece piece;
        ChessPosition cur;
        Collection<ChessMove> moves;
        for (int row = 1; row < 9; ++row) {
            for (int col = 1; col < 9; ++ col) {
                cur = new ChessPosition(row, col);
                piece = this.board.getPiece(cur);
                if (piece != null) {
                    if (piece.getTeamColor() == enemyColor) {
                        moves = piece.pieceMoves(this.board, cur);
                        for (ChessMove move : moves) {
                            if (Objects.equals(move.getEndPosition(), pos)) {
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
        ChessPosition kingPosition = getKingPosition(teamColor);
        if (kingPosition == null) return false;
        boolean isMate = isCheckPosition(kingPosition, teamColor);
        Collection<ChessMove> moves = this.board.getPiece(kingPosition).pieceMoves(this.board, kingPosition);
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
        ChessPosition kingPosition = getKingPosition(teamColor);
        if (kingPosition == null) return false;
        boolean isStalemate = !isCheckPosition(kingPosition, teamColor);
        for (ChessMove move : this.board.getPiece(kingPosition).pieceMoves(this.board, kingPosition)) {
            if (!isStalemate) break;
            isStalemate = isCheckPosition(move.getEndPosition(), teamColor);
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
