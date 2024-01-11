package chess;

import java.util.Objects;

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
     */
    public int getRow() { return this.row; }

    /**
     * @return which column this position is in
     */
    public int getColumn() { return this.col; }

     /**
     * @return if 2 positions are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ChessPosition.class) { return false; }
        ChessPosition pos = (ChessPosition) o;
        return this.row == pos.row && this.col == pos.col;
    }

    /**
     * @return Hash of position
     */
    @Override
    public int hashCode() { return Objects.hash(this.row, this.col); }

}


