package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tilt {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
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
                        if(board[r][c] == 'B'){
                            blueCount++;
                        }
                    }
                    r++;
                }
                TiltConfig tilt = new TiltConfig(dimensions, board, blueCount);
                System.out.println(tilt);
                Solver solver = new Solver();
                List<Configuration> path = new ArrayList<>();
                int num = 0;
                System.out.println("Amount: " + args[0] + ", Buckets: ");
                try {
                    path.addAll(solver.solve(tilt));
                } catch (NullPointerException npe) {
                    System.out.println("Total configs: " + solver.getTotal());
                    System.out.println("Unique configs: " + solver.getUnique());
                    System.out.println("No solution");
                    System.exit(0);
                }
                System.out.println("Total configs: " + solver.getTotal());
                System.out.println("Unique configs: " + solver.getUnique());
                if (path.get(0) == null) {
                    System.out.println("Step " + num + ": \n" + tilt);
                } else {
                    for (Configuration steps : path) {
                        System.out.println("Step " + num + ": \n" + steps);
                        num++;
                    }
                }
            }
        }
    }
}
