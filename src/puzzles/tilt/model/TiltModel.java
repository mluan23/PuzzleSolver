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
    private Tilt tilt;
    public static String LOADED = "Loaded: ";
    public static String HINT_PREFIX = "hint";
    public static String LOAD_FAILED = "load failed";
    private char[][] board;
    private int dimensions;
    public TiltModel(){
        tilt = new Tilt();
    }
    public TiltConfig getHint(){ // plug the current config into the solver, get the path, take path(1), that is the hint
        List<Configuration> path = new ArrayList<>(tilt.solveConfig(currentConfig));
        Configuration next = path.get(1);
        TiltConfig a = (TiltConfig)next;
        alertObservers(HINT_PREFIX + a);
        return a;
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
            alertObservers(LOADED + file + "\n" + t);
            return t;
        }
        catch (FileNotFoundException fnfe){
            alertObservers(LOAD_FAILED);
        }
        return null;
    }
    public static void main(String[] args) throws IOException{
        TiltModel model = new TiltModel();
        System.out.println(model.loadBoardFile(args[0]));
    }
    public int getDimensions(){
        return this.dimensions;
    }
    public void setCurrentConfig(TiltConfig config){
        this.currentConfig = config;
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
