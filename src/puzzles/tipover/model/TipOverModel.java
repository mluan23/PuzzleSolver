package puzzles.tipover.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.tipover.solver.TipOver;

import java.io.*;
import java.util.*;

public class TipOverModel {
    // Message sent when a hint is given
    public static String HINT = "Next step";

    // Message sent when a board has successfully loaded
    public static String LOADED = "Loaded";

    // Message sent when a board has failed to load
    public static String LOAD_FAILED = "Failed to load";

    // Message sent when a valid move is made
    public static String MOVE = " ";

    // Message sent when an invalid direction is inputted
    public static String INVALID = "Invalid direction inputted";

    // Message sent when a tower cannot be tipped over
    public static String NO_TIP = "Tower cannot be tipped over";

    // Message sent when a tipper tries to move to a position with no tower
    public static String NO_MOVE = "No tower there";

    // Message sent when the user quits
    public static String QUIT = "Quit";

    // Message sent when a board has been reset
    public static String RESET = "Board has been reset";

    // Message sent when a board has no solution
    public static String NO_SOLUTION = "No solution";

    // Message sent when a board has been completed
    public static String COMPLETE = "Current board is already solved";

    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TipOverConfig currentConfig;

    /** the copy configuration */
    private TipOverConfig copyConfig;

    /** the completed configuration */
    private TipOverConfig completeConfig;

    public TipOverModel() {
        this.currentConfig = null;
        this.copyConfig = null;
        this.completeConfig = null;
    }

    /**
     * Attempts to load a board from a given file name
     * It will announce to the observers if it was loaded successfully or not
     * @param file The file to load
     * @return True if the file loaded successfully
     */
    public void loadBoardFromFile(String file) {
        loadBoardFromFile(new File(file));
    }

    /**
     * Attempts to load a board from a file object
     * It will announce to the observers if it was loaded successfully or not
     * @param file The file to load
     * @return True if the file loaded successfully
     */
    public void loadBoardFromFile(File file)  {
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            String dim = input.readLine();
            String[] dimensions = dim.split("\\s+");

            int row = Integer.parseInt(dimensions[0]);
            int col = Integer.parseInt(dimensions[1]);
            int tRow = Integer.parseInt(dimensions[2]);
            int tCol = Integer.parseInt(dimensions[3]);
            int gRow = Integer.parseInt(dimensions[4]);
            int gCol = Integer.parseInt(dimensions[5]);
            char[][] board = new char[row][col];

            String line;
            int r = 0;
            while (r < row) {
                line = input.readLine();
                String[] fields = line.split("\\s+");

                for (int c = 0; c < col; c++) {
                    board[r][c] = fields[c].charAt(0);
                }

                r++;
            }

            TipOverConfig tippy = new TipOverConfig(row, col, board, tRow, tCol, gRow, gCol);
            currentConfig = tippy;
            copyConfig = tippy;

            alertObservers(LOADED + ": " + file);
        }
        catch (IOException gourd) {
            alertObservers(LOAD_FAILED + ": " + file);
        }
    }

    /**
     * Tests if the game has been won
     * The game is won when the current config is a solution
     * @return True if the current config is a solution
     */
    public boolean gameOver(){
        return this.currentConfig.isSolution();
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
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

    public void move(String direction) {
        TipOverConfig tiEightyFour = currentConfig;

        if (direction.equals("North")) {
            tiEightyFour = currentConfig.tipperMove("North");
            tiEightyFour = currentConfig.move("North");
        }
        else if (direction.equals("East")) {
            tiEightyFour = currentConfig.tipperMove("East");
            tiEightyFour = currentConfig.move("East");
        }
        else if (direction.equals("South")) {
            tiEightyFour = currentConfig.tipperMove("South");
            tiEightyFour = currentConfig.move("South");
        }
        else if (direction.equals("West")) {
            tiEightyFour = currentConfig.tipperMove("West");
            tiEightyFour = currentConfig.move("West");
        }

        if (currentConfig != null) {
            if (!gameOver()) {
                currentConfig = tiEightyFour;
            }
            alertObservers(MOVE);
        }

        else { // if an invalid direction is inputted
            alertObservers(INVALID);
        }
    }

    /**
     * @return The current config
     */
    public TipOverConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * @return The copy config
     */
    public TipOverConfig getCopyConfig() {
        return copyConfig;
    }

    public void getHint() {
        TipOver tip = new TipOver();

        try {
            List<Configuration> path = new ArrayList<>(tip.createPath(currentConfig));

            Configuration fin = path.get(0);
            completeConfig = (TipOverConfig) fin;

            Configuration next = path.get(1);
            TipOverConfig nextMove = (TipOverConfig) next;
            currentConfig = nextMove;
            alertObservers(HINT + ": \n" + nextMove);
        }
        catch (NullPointerException how) {
            alertObservers(NO_SOLUTION);
        }
        catch (IndexOutOfBoundsException wow) {
            alertObservers(COMPLETE);
            currentConfig = completeConfig;
        }
    }

    /**
     * Resets the current board to be like the copy board (which hold its original value)
     */
    public void reset() {
        currentConfig = copyConfig;
        alertObservers(RESET);
    }
}
