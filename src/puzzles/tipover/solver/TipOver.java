package puzzles.tipover.solver;

import puzzles.clock.ClockConfiguration;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;
import puzzles.tipover.model.TipOverConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TipOver {
    private List<Configuration> path;

    private Solver solver;

    public TipOver() {
        path = new ArrayList<>();
        solver = new Solver();
    }

    public TipOverConfig readFile(String file) throws IOException {
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
            return new TipOverConfig();
        }
        catch (FileNotFoundException bruh){
            System.out.println(file + " not found!");
            System.exit(0);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        }
        /** else {
            int num = 0;
            TipOverConfig tOC = new TipOverConfig();
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
        } */
    }
}
