package puzzles.tilt.model;

// TODO: implement your TiltConfig for the common solver

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.util.*;

public class TiltConfig implements Configuration {
    private final int DIM;
    private List<Configuration> neighbors;
    private char[][] board;
    private char[][] original;
    private int bCount;

    //private Map<Coordinates, String> board;
    public TiltConfig(int dimensions, char[][] board) {
        neighbors = new ArrayList<>();
        this.DIM = dimensions;
        this.board = new char[dimensions][dimensions];
        for (int row = 0; row < this.DIM; row++) {
            for(int col = 0; col < this.DIM; col++){
                this.board[row][col] = board[row][col];
                if(this.board[row][col] == 'B'){
                    bCount++;
                }
            }
        }
    }

    @Override
    public boolean isSolution() {
        int count = 0;
        for (int row = 0; row < this.DIM; row++) {
            for (int col = 0; col < this.DIM; col++) {
                if(this.board[row][col] == 'G'){
                    return false;
                }
                if(this.board[row][col] == 'B'){
                    count++;
                }
            }
        }
        if(count!=this.bCount){
            return false;
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        original = new char[DIM][DIM];
        int rows;
        int cols;
        for (int row = 0; row < DIM; row++) {
            System.arraycopy(this.board[row], 0, original[row], 0, DIM);
        }
        for(int r = 1; r<DIM; r++){
            for(int c = 0; c < DIM; c++){
                rows = r;
                cols = c;
                if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){ // up option
                    char marker = this.board[r][c];
                    while(rows-1 >= 0 && board[rows-1][cols] == '.' ){
                        rows--;
                    }
                    if(this.board[rows][cols] == 'O'){
                        if(this.board[r][c] == 'G'){
                            this.board[r][c] = '.';
                            neighbors.add(new TiltConfig(DIM, this.board));
                        }
                    }
                    else if(r!=rows){
                        this.board[r][c] = '.';
                        this.board[rows][c] = marker;
                    }
                }
            }
        }
        neighbors.add(new TiltConfig(DIM, this.board));
        this.board = original;
        for(int c = DIM-2; c >= 0 ; c--){
            for(int r = 0; r < DIM ; r++){
                rows = r;
                cols = c;
                if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){ // right option
                    char marker = this.board[r][c];
                    while(cols+1 < DIM && board[rows][cols+1] == '.' ){
                        cols++;
                    }
                    if(this.board[rows][cols] == 'O'){
                        if(this.board[r][c] == 'G'){
                            this.board[r][c] = '.';
                            neighbors.add(new TiltConfig(DIM, this.board));
                        }
                    }
                    else if(c!=cols){
                        this.board[r][c] = '.';
                        this.board[rows][c] = marker;
                    }
                }
            }
        }
        neighbors.add(new TiltConfig(DIM, this.board));
        this.board = original;
        for(int r = DIM-1; r>=0; r--){
            for(int c = 0; c < DIM; c++){
                rows = r;
                cols = c;
                if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){ // down option
                    char marker = this.board[r][c];
                    while(rows+1 < DIM && board[rows+1][cols] == '.' ){
                        rows++;
                    }
                    if(this.board[rows][cols] == 'O'){
                        if(this.board[r][c] == 'G'){
                            this.board[r][c] = '.';
                            neighbors.add(new TiltConfig(DIM, this.board));
                        }
                    }
                    else if(r!=rows){
                        this.board[r][c] = '.';
                        this.board[rows][c] = marker;
                    }
                }
            }
        }
        neighbors.add(new TiltConfig(DIM, this.board));
        this.board = original;
        return neighbors;
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
        for (int row = 0; row < DIM; row++) {
            if(row > 0) {
                result += "\n";
            }
            for (int col = 0; col < DIM; col++) {
                result += this.board[row][col] + " ";
            }
        }
        getNeighbors();
        return result;
    }
}
