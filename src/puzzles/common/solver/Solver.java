package puzzles.common.solver;

import java.util.*;

/**
 * The BFS common solver used for solving any Configuration puzzle.
 */

public class Solver {
    /** keeps track of Configurations that have already been seen and used for the building the solution path */
    private Map<Configuration, Configuration> predecessor;
    /** the number of unique configurations */
    private int unique;
    /** the total number of configurations */
    private int total;

    /**
     * Creates a Solver object. Used for solving a puzzle.
     */
    public Solver(){
        predecessor = new HashMap<>();
    }

    /**
     * Solves a given puzzle by repeatedly getting the neighbors of a given configuration,
     * until a solution is found or there is no solution.
     * @param start the starting Configuration
     * @return a LinkedList of the shortest path to the solution
     */

    public List<Configuration> solve(Configuration start) {
        predecessor.put(start, null);
        total++;
        unique++;
        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(start);
        while (!toVisit.isEmpty() && !start.isSolution()) {
            Configuration current = toVisit.remove();
            start = current;
            for (Configuration neighbor : current.getNeighbors()) {
                total++;
                if (!predecessor.containsKey(neighbor)) {
                    unique++;
                    predecessor.put(neighbor, current);
                    toVisit.offer(neighbor);
                }
            }
        }
        if (toVisit.isEmpty()) {
            return null;
        } else {
            List<Configuration> path = new LinkedList<>();
            path.add(0, start);
            Configuration c = predecessor.get(start);
            while(c != null){
                path.add(0, c);
                c = predecessor.get(c);
            }
            return path;
        }
    }

    /**
     * Gives the number of unique configurations.
     * @return unique
     */
    public int getUnique() {
        return unique;
    }

    /**
     * Gives the total number of configurations.
     * @return total
     */
    public int getTotal(){
        return total;
    }
}
