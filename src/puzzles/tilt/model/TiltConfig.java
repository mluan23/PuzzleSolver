package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.util.*;

public class TiltConfig implements Configuration {
    private int dimensions;
    private List<Configuration> neighbors;
    private char[][] board;

    //private Map<Coordinates, String> board;
    public TiltConfig(int dimensions, char[][] board) {
        this.dimensions = dimensions;
        this.board = new char[dimensions][dimensions];
        for (int row = 0; row < this.dimensions; row++) {
            for (int col = 0; col < this.dimensions; col++) {
                this.board[row][col] = board[row][col];
            }
        }
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = 0; row < dimensions; row++) {
            if(row > 0) {
                result += "\n";
            }
            for (int col = 0; col < dimensions; col++) {
                result += this.board[row][col] + " ";
            }
        }
        return result;
    }
}
