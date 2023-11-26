package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.solver.Tilt;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    private TiltConfig originalConfig;
    private TiltConfig finishedConfig;
    public static String LOADED = "Loaded: ";
    public static String HINT_PREFIX = "Next step! \n";
    public static String LOAD_FAILED = "Failed to load: ";
    public static String NO_SOLUTION = "No solution! \n";
    public static String FINISHED = "Already solved!";
    public static String MOVE = "Tilted";
    public static String ILLEGAL = "Illegal move. A blue slider will fall through the hole!";
    public static String RESET = "Puzzle Reset! \n";
    private char[][] board;
    public TiltModel(){
        currentConfig = null;
        finishedConfig = null;
        originalConfig = null;
    }

    public void getHint(){
        Tilt tilt = new Tilt();
        try {
            List<Configuration> path = new ArrayList<>(tilt.solveConfig(currentConfig));
            Configuration finished = path.get(0);
            TiltConfig finish = (TiltConfig) finished;
            finishedConfig = finish;
            Configuration n = path.get(1);
            TiltConfig next = (TiltConfig) n;
            currentConfig = next;
            alertObservers(HINT_PREFIX + next);
        }
        catch (NullPointerException npe){
            alertObservers(NO_SOLUTION + currentConfig + "\n");
        }
        catch (IndexOutOfBoundsException e){
            alertObservers(FINISHED + "\n" + currentConfig + "\n");
            currentConfig = finishedConfig;
        }
    }
    public void reset(){
        currentConfig = originalConfig;
        alertObservers(RESET);
    }
    public TiltConfig loadBoardFile(String file) throws IOException{
        return loadBoardFile(new File(file));
    }
    public TiltConfig loadBoardFile(File file) throws IOException {
        try(BufferedReader in = new BufferedReader(new FileReader(file))){
            int r = 0;
            int blueCount = 0;
            String f = in.readLine();
            int dimensions = Integer.parseInt(f);
            this.board = new char[dimensions][dimensions];
            String line;
            while((line = in.readLine()) != null){
                String[] fields = line.split("\\s+");
                for (int c = 0; c < dimensions; ++c) {
                    this.board[r][c] = fields[c].charAt(0);
                    if(this.board[r][c] == 'B' || this.board[r][c] == 'G' ||
                            this.board[r][c] == '*' || this.board[r][c] == '.' || this.board[r][c] == 'O'){
                        if(this.board[r][c] == 'B'){
                            blueCount++;
                        }
                    }
                    else{
                        alertObservers(LOAD_FAILED);
                        return null;
                    }
                }
                r++;
            }
            TiltConfig t = new TiltConfig(dimensions,this.board, blueCount);
            originalConfig = t;
            alertObservers(LOADED + file);
            return t;
        }
        catch (FileNotFoundException fnfe){
            alertObservers(LOAD_FAILED + file);
        }
        return null;
    }
    public TiltConfig makeMove(String direction){
        TiltConfig move = currentConfig;
        if(direction.equals("north")){
            move = currentConfig.up();
        } else if (direction.equals("south")) {
            move = currentConfig.down();

        }  else if (direction.equals("east")) {
            move = currentConfig.right();

        }  else if (direction.equals("west")) {
            move = currentConfig.left();
        }
        if(move!=null) {
            currentConfig = move;
            alertObservers(MOVE);
        }
        else{
            alertObservers(ILLEGAL);
        }
        return move;
    }
    public void setCurrentConfig(TiltConfig config){
        this.currentConfig = config;
    }
    public TiltConfig getCurrentConfig(){
        return this.currentConfig;
    }
    public TiltConfig getOriginalConfig(){
        return originalConfig;
    }
    public boolean gameOver(){
        return this.currentConfig.isSolution();
    }



    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
