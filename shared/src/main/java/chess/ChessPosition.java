package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() { return this.row; }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() { return this.col; }

     /**
     * Compares 2 positions
     * @return if positions are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessPosition.class) { return false; }
        ChessPosition pos = (ChessPosition) o;
        return this.row == pos.row & this.col == pos.col;
    }

    /**
     * @return Hash of position
     */
    @Override
    public int hashCode() { return this.row * 8 + this.col; }

}


