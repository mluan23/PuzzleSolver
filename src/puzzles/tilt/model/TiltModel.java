package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.solver.Tilt;

import java.io.*;
import java.util.*;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    /** the configuration to represent to starting configuration of a given file */
    private TiltConfig originalConfig;
    /** the configuration to represent the solution to a solved puzzle */
    private TiltConfig finishedConfig;
    /** the message that indicates a board has successfully been loaded */
    public static String LOADED = "Loaded: ";
    /** the message that indicates a hint is given */
    public static String HINT = "Next step! \n";
    /** the message that indicates the board was not loaded */
    public static String LOAD_FAILED = "Failed to load: ";
    /** the message that indicates there is no solution to the puzzle */
    public static String NO_SOLUTION = "No solution! \n";
    /** the message that indicates a board has been solved */
    public static String FINISHED = "Already solved!";
    /** the message that indicates a move has been made */
    public static String MOVE = "Tilted";
    /** the message that indicates the move being made is illegal */
    public static String ILLEGAL = "Illegal move. A blue slider will fall through the hole!";
    /** the message that indicates the board is reset */
    public static String RESET = "Puzzle reset!";
    /** the representation of the game board */
    private char[][] board;
    public TiltModel(){
        currentConfig = null;
        finishedConfig = null;
        originalConfig = null;
    }

    /**
     * Gives the next config in the solution path. Used for giving hints.
     */

    public void getHint(){
        Tilt tilt = new Tilt();
        try {
            List<Configuration> path = new ArrayList<>(tilt.solveConfig(currentConfig));
            Configuration finished = path.get(0);
            finishedConfig = (TiltConfig) finished;
            Configuration n = path.get(1);
            TiltConfig next = (TiltConfig) n;
            currentConfig = (TiltConfig) n;
            alertObservers(HINT + next);
        }
        catch (NullPointerException npe){ // solving returns null when no solution
            alertObservers(NO_SOLUTION + currentConfig + "\n");
        }
        catch (IndexOutOfBoundsException e){
            alertObservers(FINISHED + "\n" + currentConfig + "\n");
            currentConfig = finishedConfig;
        }
    }

    /**
     * Resets the game board by changing the current config to the starting config.
     */
    public void reset(){
        currentConfig = originalConfig;
        alertObservers(RESET);
    }

    /**
     * Loads the board from a given filename.
     * @param file the file to load from
     */
    public void loadBoardFile(String file) {
        loadBoardFile(new File(file));
    }

    /**
     * Loads the board from a given file.
     * @param file the file to load from
     */
    public void loadBoardFile(File file) {
        try(BufferedReader in = new BufferedReader(new FileReader(file))){
            int r = 0;
            int blueCount = 0;
            String dim = in.readLine();
            int dimensions = Integer.parseInt(dim);
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
                    }
                }
                r++;
            }
            TiltConfig t = new TiltConfig(dimensions,this.board, blueCount);
            originalConfig = t;
            currentConfig = t;
            alertObservers(LOADED + file);
        }
        catch (NumberFormatException | IOException e ){
            alertObservers(LOAD_FAILED + file);
        }
    }


    /**
     * Allows the user to tilt the board in one of four directions.
     * @param direction the direction to be tilted in
     * @return the TiltConfig representing the board after it was tilted
     */
    public TiltConfig makeMove(String direction){
        TiltConfig move = currentConfig;
        if(direction.equals("north")){
            move = currentConfig.move("north");
        } else if (direction.equals("south")) {
            move = currentConfig.move("south");

        }  else if (direction.equals("east")) {
            move = currentConfig.move("east");

        }  else if (direction.equals("west")) {
            move = currentConfig.move("west");
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

    /**
     * Sets the current config to a given config.
     * @param config the config to be set equal to
     */
    public void setCurrentConfig(TiltConfig config){
        this.currentConfig = config;
    }

    /**
     * Gives the current config.
     * @return currentConfig
     */
    public TiltConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * Gives the starting config.
     * @return originalConfig
     */
    public TiltConfig getOriginalConfig(){
        return originalConfig;
    }

    /**
     * The game is over when the current config is a solution.
     * @return true if the current config is a solution, false otherwise
     */
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
