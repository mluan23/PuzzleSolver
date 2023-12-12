package puzzles.tipover.model;

import puzzles.common.solver.Configuration;

import java.util.*;

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
        this.board = new char[ROW][COL];
        this.copy = new char[ROW][COL];
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                char value = board[r][c];
                this.board[r][c] = value;
                this.copy[r][c] = value;
            }
        }
    }

    @Override
    public boolean isSolution() {
        return tipperRow == goalRow && tipperCol == goalCol;
    }

    /**
     * Possible neighbors include tipping the tower N, E, S, W, or moving the tipper to another tower/another part
     * of a tower (can also be N, E, S, W)
     * @return
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        TipOverConfig tNorth = this.tipperMove("North");
        TipOverConfig tEast = this.tipperMove("East");
        TipOverConfig tSouth = this.tipperMove("South");
        TipOverConfig tWest = this.tipperMove("West");

        TipOverConfig north = this.move("North");
        TipOverConfig east = this.move("East");
        TipOverConfig south = this.move("South");
        TipOverConfig west = this.move("West");

        if (tNorth != null) {
            neighbors.add(tNorth);
        }
        if (tEast != null) {
            neighbors.add(tEast);
        }
        if (tSouth != null) {
            neighbors.add(tSouth);
        }
        if (tWest != null) {
            neighbors.add(tWest);
        }

        if (north != null) {
            neighbors.add(north);
        }
        if (east != null) {
            neighbors.add(east);
        }
        if (south != null) {
            neighbors.add(south);
        }
        if (west != null) {
            neighbors.add(west);
        }

        return neighbors;
    }

    /**
     *
     * @param direction
     * @return
     */
    public TipOverConfig tipperMove(String direction) {
        int row = tipperRow;
        int col = tipperCol;

        switch (direction) {
            case "North" -> {
                if (row > 0 && board[row - 1][col] > '0') {
                    row -= 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                return neighbor;
            }
            case "East" -> {
                if (col < COL - 1 && board[row][col + 1] > '0') {
                    col += 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                return neighbor;
            }
            case "South" -> {
                if (row < ROW - 1 && board[row + 1][col] > '0') {
                    row += 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                return neighbor;
            }
            case "West" -> {
                if (col > 0 && board[row][col - 1] > '0') {
                    col -= 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                return neighbor;
            }
        }
        return null;
    }

    public TipOverConfig move(String direction) {
        int row = tipperRow;
        int col = tipperCol;

        switch (direction) {
            case "North" -> {
                int tower = this.board[row][col] - '0';
                int counter = 0;

                if (tower < 2) {
                    return null;
                }

                if (row - tower >= 0) {
                    for (int i = 1; i <= tower; i++) {
                        counter++;
                        if (board[row - i][col] != '0') {
                            return null;
                        }
                    }
                    if (tower <= counter) {
                        for (int j = 1; j <= tower; j++) {
                            board[row - j][col] = '1';
                        }
                    }
                    board[row][col] = '0';
                    row -= 1;
                }
                else {
                    return null;
                }

                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                this.resetBoard();
                return neighbor;

            }
            case "East" -> {
                int tower = this.board[row][col] - '0';
                int counter = 0;

                if (tower < 2) {
                    return null;
                }

                if (col + tower < COL) {
                    for (int i = 1; i <= tower; i++) {
                        counter++;
                        if (board[row][col + i] != '0') {
                            return null;
                        }
                    }
                    if (tower <= counter) {
                        for (int j = 1; j <= tower; j++) {
                            board[row][col + j] = '1';
                        }
                    }
                    board[row][col] = '0';
                    col += 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                this.resetBoard();
                return neighbor;
            }
            case "South" -> {
                int tower = this.board[row][col] - '0';
                int counter = 0;

                if (tower < 2) {
                    return null;
                }

                if (row + tower < ROW) {
                    for (int i = 1; i <= tower; i++) {
                        counter++;
                        if (board[row + i][col] != '0') {
                            return null;
                        }
                    }
                    if (tower <= counter) {
                        for (int j = 1; j <= tower; j++) {
                            board[row + j][col] = '1';
                        }
                    }
                    board[row][col] = '0';
                    row += 1;
                }
                else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                this.resetBoard();
                return neighbor;
            }
            case "West" -> {
                int tower = this.board[row][col] - '0';
                int counter = 0;

                if (tower < 2) {
                    return null;
                }

                if (col - tower >= 0) {
                    for (int i = 1; i <= tower; i++) {
                        counter++;
                        if (board[row][col - i] != '0') {
                            return null;
                        }
                    }
                    if (tower <= counter) {
                        for (int j = 1; j <= tower; j++) {
                            board[row][col - j] = '1';
                        }
                    }
                    board[row][col] = '0';
                    col -= 1;
                } else {
                    return null;
                }
                TipOverConfig neighbor = new TipOverConfig(ROW, COL, board, row, col, goalRow, goalCol);
                this.resetBoard();
                return neighbor;
            }
        }
        return null;
    }

    public void resetBoard() {
        for (int row = 0; row < ROW; row++){
            System.arraycopy(copy[row], 0, this.board[row], 0, COL);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TipOverConfig) {
            TipOverConfig top = (TipOverConfig) other;
            return Arrays.deepEquals(this.board, top.board) && tipperRow == top.tipperRow && tipperCol == top.tipperCol;
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

        // super inefficient way to do the headers for the board (womp)
        for (int header = 0; header < 2; header++) {
            if (header > 0) {
                gBoard += "\n";
            }
            gBoard += "   ";
            if (header == 0) {
                for (int col = 0; col < COL; col++) {
                    gBoard += "  " + col;
                }
            }
            else {
                for (int col = 0; col < COL; col++) {
                    gBoard += "___";
                }
            }
        }

        gBoard += "\n";

        for (int row = 0; row < ROW; row++) {
            if (row > 0) {
                gBoard += "\n";
            }
            for (int col = 0; col < COL; col++) {
                if (col == 0) {
                    if (tipperRow == row && tipperCol == col) {
                        gBoard += row + " | *" + this.board[row][col];
                    }
                    else if (goalRow == row && goalCol == col) {
                        gBoard += row + " | !" + this.board[row][col];
                    }
                    else if (this.board[row][col] > '0') {
                        gBoard += row + " |  " + this.board[row][col];
                    }
                    else {
                        gBoard += row + " |  _";
                    }
                }
                else {
                    if (tipperRow == row && tipperCol == col) {
                        gBoard += " *" + this.board[row][col];
                    }
                    else if (goalRow == row && goalCol == col) {
                        gBoard += " !" + this.board[row][col];
                    }
                    else if (this.board[row][col] > '0') {
                        gBoard += "  " + this.board[row][col];
                    }
                    else {
                        gBoard += "  _";
                    }
                }
            }
        }

        return gBoard;
    }
}