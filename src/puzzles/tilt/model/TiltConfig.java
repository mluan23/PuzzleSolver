package puzzles.tilt.model;

import puzzles.common.solver.Configuration;
import java.util.*;

/**
 * The representation for a single TiltConfig for the Tilt puzzle.
 */

public class TiltConfig implements Configuration {
    /** the dimensions of the board */
    private final int DIM;
    /** the neighboring configs */
    private List<Configuration> neighbors;
    /** represents the board of the config */
    private char[][] board;
    /** the starting board of the given config */
    private char[][] original;
    /** the number of blue disks on the board */
    private int blueCount;

    /**
     * Creates a TiltConfig, with two copies of the board being made.
     * @param dimensions the dimensions of the board
     * @param board the board
     * @param blueCount the number of blue disks
     */
    public TiltConfig(int dimensions, char[][] board, int blueCount) {
        this.blueCount = blueCount;
        neighbors = new ArrayList<>();
        this.DIM = dimensions;
        original = new char[DIM][DIM];
        this.board = new char[DIM][DIM];
        for (int row = 0; row < this.DIM; row++) {
            for(int col = 0; col < this.DIM; col++){
                this.board[row][col] = board[row][col];
                original[row][col] = board[row][col];
            }
        }
    }

    /**
     * The given TiltConfig is a solution if there are no green disks.
     * @return true if the config is a solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        for (int row = 0; row < this.DIM; row++) {
            for (int col = 0; col < this.DIM; col++) {
                if(this.board[row][col] == 'G'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gives the config if the board were to be tilted in the given direction, only if it does not result in a blue
     * disk falling into the hole.
     * @param direction the direction to tilt the board
     * @return the config for the direction the board was tilted in or null if the move is illegal
     */
    public TiltConfig move(String direction){
        int rows;
        int cols;
        if(direction.equals("north")) {
            for (int r = 1; r < DIM; r++) {
                for (int c = 0; c < DIM; c++) {
                    rows = r;
                    cols = c;
                    if (this.board[r][c] == 'G' || this.board[r][c] == 'B') {
                        char marker = this.board[r][c];
                        while (rows - 1 >= 0 && board[rows - 1][cols] == '.') {
                            rows--;
                        }
                        if (rows - 1 >= 0 && this.board[rows - 1][cols] == 'O') { // the slider fell into the hole
                            this.board[r][c] = '.';
                        } else if (r != rows) {
                            this.board[r][c] = '.';
                            this.board[rows][c] = marker;
                        }
                    }
                }
            }
        } else if (direction.equals("east")) {
            for(int c = DIM-2; c >= 0 ; c--){
                for(int r = 0; r < DIM ; r++){
                    rows = r;
                    cols = c;
                    if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){
                        char marker = this.board[r][c];
                        while(cols+1 < DIM && board[rows][cols+1] == '.' ){
                            cols++;
                        }
                        if (cols+1<DIM && this.board[rows][cols + 1] == 'O') {
                            this.board[r][c] = '.';
                        }
                        else if(c!=cols){
                            this.board[r][c] = '.';
                            this.board[r][cols] = marker;
                        }
                    }
                }
            }
        } else if (direction.equals("south")) {
            for(int r = DIM-2; r>=0; r--){
                for(int c = 0; c < DIM; c++){
                    rows = r;
                    cols = c;
                    if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){
                        char marker = this.board[r][c];
                        while(rows+1 < DIM && board[rows+1][cols] == '.' ){
                            rows++;
                        }
                        if (rows+1<DIM && this.board[rows + 1][cols] == 'O') {
                            this.board[r][c] = '.';
                        }
                        else if(r!=rows){
                            this.board[r][c] = '.';
                            this.board[rows][c] = marker;
                        }
                    }
                }
            }
        } else if (direction.equals("west")) {
            for(int c = 1; c<DIM; c++){
                for(int r = 0; r < DIM; r++){
                    rows = r;
                    cols = c;
                    if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){
                        char marker = this.board[r][c];
                        while(cols-1 >= 0 && board[rows][cols-1] == '.' ){
                            cols--;
                        }
                        if (cols-1 >= 0 && this.board[rows][cols-1] == 'O') {
                            this.board[r][c] = '.';
                        }
                        else if(c!=cols){
                            this.board[r][c] = '.';
                            this.board[r][cols] = marker;
                        }
                    }
                }
            }
        }
        if(countBlue()==blueCount) {
            TiltConfig next = new TiltConfig(DIM, this.board, blueCount);
            this.reset();
            return next;
        }
        this.reset();
        return null;
    }

    /**
     * Creates a copy array using the original config.
     */
    private void reset(){
        for (int row = 0; row<DIM; row++){
            System.arraycopy(original[row],0,this.board[row],0,DIM);
        }
    }

    /**
     * Gives the neighbors of a given config.
     * @return an ArrayList of the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        if (this.move("east") != null) {
            neighbors.add(this.move("east"));
        }
        if (this.move("south") != null) {
            neighbors.add(this.move("south"));
        }
        if (this.move("north") != null) {
            neighbors.add(this.move("north"));
        }
        if (this.move("west") != null) {
            neighbors.add(this.move("west"));
        }
        return neighbors;
    }

    /**
     * Gives the dimensions of the board.
     * @return DIM
     */
    public int getDimensions(){
        return DIM;
    }

    /**
     * Counts the number of blue disks on a board.
     * @return the number of blue disks
     */
    public int countBlue(){
        int count = 0;
        for(int row = 0; row<DIM;row++){
            for(int col=0;col<DIM;col++){
                if(this.board[row][col] == 'B'){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gives the board of the current config.
     * @return the board
     */
    public char[][] getBoard(){
        return this.board;
    }

    /**
     * Two TiltConfigs are equal if their boards are equal.
     * @param other the other config to compare to
     * @return true if the configs are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof TiltConfig){
            TiltConfig config = (TiltConfig) other;
            return Arrays.deepEquals(this.board, config.board);
        }
        return false;
    }

    /**
     * The hashcode of the TiltConfig is equal to the hashcode of the board.
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Represents each TiltConfig as the board.
     * @return the String representation of the TiltConfig
     */
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
        return result;
    }
}
