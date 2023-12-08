package puzzles.tipover.model;

// TODO: implement your TipOverConfig for the common solver

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

    public void reset() {
        // copy the copy of the board over the current board (go figure it out)
    }

    @Override
    public boolean isSolution() {
        return board[tipperRow][tipperCol] == board[goalRow][goalCol];
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        return neighbors;
    }

    // public TipOverConfig tipTower() {}

    // public TipOverConfig move() {}

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

    @Override
    public String toString() {
        return null;
    }
}
