package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.solver.Tilt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    public static String LOADED = "loaded";
    public TiltModel(TiltConfig currentConfig){
        this.currentConfig = currentConfig;

    }
    public TiltConfig getHint(){ // plug the current config into the solver, get the path, take path(1), that is the hint
        Solver solver = new Solver();
        List<Configuration> path = new ArrayList<>(solver.solve(currentConfig));
        Configuration next = path.get(1);



        return null;
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
