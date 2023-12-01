package puzzles.tilt.model;

import puzzles.common.solver.Configuration;
import java.util.*;

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
    public TiltConfig up(){
        int rows;
        int cols;
        for(int r = 1; r<DIM; r++){
            for(int c = 0; c < DIM; c++){
                rows = r;
                cols = c;
                if(this.board[r][c] == 'G' || this.board[r][c] == 'B'){
                    char marker = this.board[r][c];
                    while(rows-1 >= 0 && board[rows-1][cols] == '.' ){
                        rows--;
                    }
                    if (rows-1 >= 0&& this.board[rows - 1][cols] == 'O') { // the slider fell into the hole
                        this.board[r][c] = '.';
                    }
                    else if(r!=rows){
                        this.board[r][c] = '.';
                        this.board[rows][c] = marker;
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
    public TiltConfig right(){
        int rows;
        int cols;
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
        if(countBlue()==blueCount) {
            TiltConfig next = new TiltConfig(DIM, this.board, blueCount);
            this.reset();
            return next;
        }
        this.reset();
        return null;
    }
    public TiltConfig down(){
        int rows;
        int cols;
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
        if(countBlue()==blueCount) {
            TiltConfig next = new TiltConfig(DIM, this.board, blueCount);
            this.reset();
            return next;
        }
        this.reset();
        return null;
    }
    public TiltConfig left(){
        int rows;
        int cols;
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
        if(countBlue()==blueCount) {
            TiltConfig next = new TiltConfig(DIM, this.board, blueCount);
            this.reset();
            return next;
        }
        this.reset();
        return null;
    }
    private void reset(){
        for (int row = 0; row<DIM; row++){
            System.arraycopy(original[row],0,this.board[row],0,DIM);
        }
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        if (this.right() != null) {
            neighbors.add(this.right());
        }
        if (this.down() != null) {
            neighbors.add(this.down());
        }
        if (this.up() != null) {
            neighbors.add(this.up());
        }
        if (this.left() != null) {
            neighbors.add(this.left());
        }
        return neighbors;
    }
    public int getDimensions(){
        return DIM;
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof TiltConfig){
            TiltConfig config = (TiltConfig) other;
            return Arrays.deepEquals(this.board, config.board);
        }
        return false;
    }
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
    public char[][] getBoard(){
        return this.board;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
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
        return result;
    }
}
