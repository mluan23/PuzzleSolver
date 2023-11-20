package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * CSAPX Project 2-1: Clock and Water Puzzles
 * Using a common BFS solver, solves two puzzles in the least number of steps.
 *
 * Main class for the clock puzzle.
 *
 * @author Matthew Luan
 */
public class Clock {
    /**
     * Run an instance of the clock puzzle.
     *
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            int num = 0;
            ClockConfiguration clock = new ClockConfiguration
                    (Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            System.out.println(clock);
            Solver solver = new Solver();
            List<Configuration> path = new ArrayList<>();
            try{
                path.addAll(solver.solve(clock));
            }
            catch(NullPointerException npe){
                System.out.println("Total configs: " + solver.getTotal());
                System.out.println("Unique configs: " + solver.getUnique());
                System.out.println("No solution");
                System.exit(0);
            }
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: " + solver.getUnique());
            if(path.get(0) == null){
                System.out.println("Step " + num + ": " + clock.hashCode());
            }
            else {
                for (Configuration steps : path) {
                    System.out.println("Step " + num + ": " + steps.hashCode());
                    num++;
                }
            }
        }
    }
}
