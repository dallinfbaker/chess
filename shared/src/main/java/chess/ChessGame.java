package chess;
import java.util.*;
import java.lang.Math;

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
    private ChessPiece enPassantVulnerable = null;
    private boolean finished;
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        pieceCounter = new HashMap<>();
        pieceCounter.put(TeamColor.WHITE, 16);
        pieceCounter.put(TeamColor.BLACK, 16);
        finished = false;
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
        BLACK,
        STALEMATE
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
            Collection<ChessMove> moves = piece.pieceMoves(this.board);
            TeamColor color = this.turnColor;
            this.setTeamTurn(piece.getTeamColor());
            moves.removeIf(move -> !isValidMove(move));
            this.setTeamTurn(color);
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
            if (enPassantVulnerable != null) { enPassantVulnerable.setEnPassantVulnerable(false); }
            enPassantVulnerable = board.getPiece(move.getEndPosition());
            if (enPassantVulnerable.getPieceType() == ChessPiece.PieceType.PAWN &&
                    Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) == 2)
            { enPassantVulnerable.setEnPassantVulnerable(true); }
            else { enPassantVulnerable = null; }
        }
        else throw new InvalidMoveException();
        this.setTeamTurn(this.turnColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private boolean isValidMove(ChessMove move) {
        ChessPiece movePiece = this.board.getPiece(move.getStartPosition());
        if (movePiece == null) return false;
        else if (movePiece.getTeamColor() != this.turnColor) return false;
        else if (!movePiece.pieceMoves(this.board).contains(move)) return false;
        else if (movePiece.getPieceType() == ChessPiece.PieceType.KING &&
                Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) == 2) {
            int row = move.getStartPosition().getRow(),  end = move.getEndPosition().getColumn(), direction = end == 7 ? 1 : -1;
            for (int col = move.getStartPosition().getColumn(); col * direction <= end * direction; col += direction) {
                if (isCheckPosition(new ChessPosition(row, col), this.turnColor)) return false;
            }
        }
        boolean valid = true;
        this.board.movePiece(move);
        if (isInCheck(this.turnColor)) valid = false;
        this.board.undoMove(move);
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
        else return isCheckPosition(king.getPosition(), king.getTeamColor());
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
        ChessPiece king = this.board.getKing(teamColor);
        if (king == null) return false;
        boolean isMate = isCheckPosition(king.getPosition(), king.getTeamColor());
        isMate = validMoves(king.getPosition()).isEmpty() && isMate;
        finished = isMate;
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
        boolean isStalemate = !isCheckPosition(king.getPosition(), king.getTeamColor());
        ChessPosition cur;
        for (ChessMove move : king.pieceMoves(this.board)) {
            if (!isStalemate) break;
            cur = move.getEndPosition();
            isStalemate = isCheckPosition(cur, king.getTeamColor());
        }
        finished = isStalemate;
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
    public boolean getFinished() { return finished; }
    public void resign(TeamColor color) {
        turnColor = Objects.equals(color, TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        finished = true;
    }
    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board.toString() +
                ", turnColor=" + turnColor +
                ", pieceCounter=" + pieceCounter +
                ", enPassantVulnerable=" + enPassantVulnerable +
                '}';
    }
}
