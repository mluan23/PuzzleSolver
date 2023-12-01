package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSAPX Project 2-2: Tilt Puzzle
 * Using a common BFS solver, solves the Tilt Puzzle.
 *
 * Main class for Tilt Puzzle.
 *
 * @author Matthew Luan
 */

public class Tilt {
    /** the solution path for a given configuration */
    private List<Configuration> path;
    /** the Solver that solves the puzzle */
    private Solver solver;

    public Tilt() {
        path = new ArrayList<>();
        solver = new Solver();
    }

    /**
     * Solves the given configuration.
     * @param current the configuration to solve
     * @return the solution path if it exists, null if there is no solution
     */

    public List<Configuration> solveConfig(TiltConfig current) {
        try {
            path.addAll(solver.solve(current));
        } catch (NullPointerException npe) {
            return null;
        }
        return path;
    }

    /**
     *
     * @param filename
     * @return
     */

    public TiltConfig readFile(String filename) {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            int blueCount = 0;
            int r = 0;
            String f = in.readLine();
            int dimensions = Integer.parseInt(f);
            char[][] board = new char[dimensions][dimensions];
            String line;
            while ((line = in.readLine()) != null) {
                String[] fields = line.split("\\s+");
                for (int c = 0; c < dimensions; ++c) {
                    board[r][c] = fields[c].charAt(0);
                    if (board[r][c] == 'B') {
                        blueCount++;
                    }
                }
                r++;
            }
            return new TiltConfig(dimensions, board, blueCount);
        }
        catch(IOException e){
            System.out.println(filename + " not found!");
            System.exit(0);
        }
        return null;
    }

    /**
     * Run an instance of the Tilt puzzle.
     *
     * @param args [0] the file to load the puzzle from
     */

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        } else {
            Tilt t = new Tilt();
            TiltConfig con = t.readFile(args[0]);
            int num = 0;
            System.out.println("File: " + args[0]);
            System.out.println(con);
            if (t.solveConfig(con) == null) {
                System.out.println("Total configs: " + t.solver.getTotal());
                System.out.println("Unique configs: " + t.solver.getUnique());
                System.out.println("No solution");
            } else {
                System.out.println("Total configs: " + t.solver.getTotal());
                System.out.println("Unique configs: " + t.solver.getUnique());
                if (t.path.get(0) == null) {
                    System.out.println("Step " + num + ": \n" + con);
                } else {
                    for (Configuration steps : t.path) {
                        System.out.println("Step " + num + ": \n" + steps + "\n");
                        num++;
                    }
                }
            }
        }
    }
}

