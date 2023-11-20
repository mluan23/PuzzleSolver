package puzzles.tilt.solver;

import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tilt {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
                int r = 0;
                String f = in.readLine();
                int dimensions = Integer.parseInt(f);
                char[][] board = new char[dimensions][dimensions];
                String line;
                while ((line = in.readLine()) != null) {
                    String[] fields = line.split("\\s+");
                    for (int c = 0; c < dimensions; ++c) {
                        board[r][c] = fields[c].charAt(0);
                    }
                    r++;
                }
                TiltConfig config = new TiltConfig(dimensions, board);
                System.out.println(config);
            }
        }
    }
}
