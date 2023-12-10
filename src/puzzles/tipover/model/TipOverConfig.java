package puzzles.tipover.model;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TipOverConfig implements Configuration {
    private final int ROW;

    private final int COL;

    private int tipperRow;

    private int tipperCol;

    private int goalRow;

    private int goalCol;

    private List<Configuration> neighbors;

    private char[][] board;

    private char[][] copy;

    public TipOverConfig(int row, int col, char[][] board, int tipperRow, int tipperCol, int goalRow, int goalCol) {
        this.ROW = row;
        this.COL = col;
        this.tipperRow = tipperRow;
        this.tipperCol = tipperCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;

        this.neighbors = new ArrayList<>();
        this.copy = new char[ROW][COL];
        this.board = new char[ROW][COL];
        for (int r = 0; r < this.ROW; r++) {
            for(int c = 0; c < this.COL; c++){
                this.board[r][c] = board[r][c];
                this.copy[r][c] = board[r][c];
            }
        }
    }

    @Override
    public boolean isSolution() {
        return board[tipperRow][tipperCol] == board[goalRow][goalCol];
    }

    /**
     * Possible neighbors include tipping the tower N, E, S, W, or moving the tipper to another tower/another part
     * of a tower (can also be N, E, S, W)
     * @return
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        if (this.move("North") != null) {
            neighbors.add(this.move("North"));
        }
        else if (this.move("East") != null) {
            neighbors.add(this.move("East"));
        }
        else if (this.move("South") != null) {
            neighbors.add(this.move("South"));
        }
        else if (this.move("West") != null) {
            neighbors.add(this.move("West"));
        }

        return neighbors;
    }
/**
    public TipOverConfig move(String direction) {
        if (direction.equalsIgnoreCase("North")) {
            if ()
        }

        else if (direction.equalsIgnoreCase("East")) {

        }

        else if (direction.equalsIgnoreCase("South")) {

        }

        else if (direction.equalsIgnoreCase("West")) {

        }

        this.resetBoard();
        return null;
    }
 */

    public void resetBoard() {
        for (int r = 0; r < ROW; r++){
            System.arraycopy(copy[r], 0, this.board[r], 0, ROW);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TipOverConfig) {
            TipOverConfig top = (TipOverConfig) other;
            return Arrays.deepEquals(this.board, top.board);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * example below (for my own sake)
     *      0  1  2  3  4  5
     *    __________________
     * 0 |  _  1  1  1  1 !1
     * 1 |  _  _  _  _  1  1
     * 2 |  _  _  1  _ *1  1
     * 3 |  3  1  _  _  2  _
     * @return A string representation of the board with the number of rows and columns for the given board, the towers
     * and their heights, the location of the tipper, and the location of the goal
     */
    @Override
    public String toString() {
        String gBoard = "";
        /** needs refinement

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (r == 0) {
                    counter = 0;
                    if (c == 5 || c )
                    board += "\n";
                }
                if (r == 1) {
                    if (c > 3) {
                        board += "-";
                    }
                }
                if (c == 0) {
                    board += c;
                }
                if (c == 1) {
                    board += "|";
                }
            }
        }
        */

        for (int row = 0; row < ROW; row++) {
            if (row > 0) {
                gBoard += "\n";
            }
            for (int col = 0; col < COL; col++) {
                if (this.board[row][col] == this.board[tipperRow][tipperCol]) {
                    gBoard += " *" + this.board[row][col];
                }
                if (this.board[row][col] == this.board[goalRow][goalCol]) {
                    gBoard += " !" + this.board[row][col];
                }
                else {
                    gBoard += "  _";
                }
            }
        }

        return gBoard;
    }
}
