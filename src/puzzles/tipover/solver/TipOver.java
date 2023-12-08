package puzzles.tipover.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tipover.model.TipOverConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSAPX Project 2-2: TipOver Puzzle
 * Using the common Solver class, the TiltOver puzzle will be solved with BFS
 *
 * @author Raymond Lee
 */

public class TipOver {
    private List<Configuration> path;

    private Solver solver;

    public TipOver() {
        path = new ArrayList<>();
        solver = new Solver();
    }

    public TipOverConfig readFile(String file) {
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
            while ((line = input.readLine()) != null) {
                String[] fields = line.split("\\s+");
                for (int r = 0; r < row; r++) {
                    for (int c = 0; c < col; c++) {
                        board[r][c] = fields[c].charAt(0);
                    }
                }
            }
            // should create a new TipOverConfig that takes in the dimensions, the board,
            // the starting location, and the goal location
            return new TipOverConfig(row, col, board, tRow, tCol, gRow, gCol);
        }
        catch (IOException bruh) {
            System.out.println(file + " not found!");
            System.exit(0);
        }
        return null;
    }

    /**
     * Attempts to create a path for the current config
     * @param config the current config
     * @return a path if there is a solution, otherwise it returns an empty list
     */
    public List<Configuration> createPath(TipOverConfig config) {
        try {
            path.addAll(solver.solve(config));
        }
        catch (NullPointerException womp) {
            return null;
        }
        return path;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        }
        else {
            TipOver tip = new TipOver();
            TipOverConfig top = tip.readFile(args[0]);

            int move = 0;

            System.out.println("File: " + args[0]);
            System.out.println(top);

            if (tip.createPath(top) == null) {
                System.out.println("Total configs: " + tip.solver.getTotal());
                System.out.println("Unique configs: " + tip.solver.getUnique());
                System.out.println("No solution");
            }
            else {
                System.out.println("Total configs: " + tip.solver.getTotal());
                System.out.println("Unique configs: " + tip.solver.getUnique());
                if (tip.path.get(0) == null) {
                    System.out.println("Step " + move + ": \n" + top);
                }
                else {
                    for (Configuration steps : tip.path) {
                        System.out.println("Step " + move + ": \n" + steps + "\n");
                        move++;
                    }
                }
            }
        }
    }
}
