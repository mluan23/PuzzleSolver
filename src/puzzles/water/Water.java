package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Matthew Luan
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            List<Bucket> bucketList = new ArrayList<>();
            List<Bucket> b = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                bucketList.add(new Bucket(0, Integer.parseInt(args[i])));
                b.add(new Bucket(Integer.parseInt(args[i]),Integer.parseInt(args[i])));
            }
            WaterConfiguration water = new WaterConfiguration(Integer.parseInt(args[0]), bucketList);
            Solver solver = new Solver();
            List<Configuration> path = new ArrayList<>();
            int num = 0;
            System.out.println("Amount: " + args[0] + ", Buckets: " + b);
            try {
                path.addAll(solver.solve(water));
            } catch (NullPointerException npe) {
                System.out.println("Total configs: " + solver.getTotal());
                System.out.println("Unique configs: " + solver.getUnique());
                System.out.println("No solution");
                System.exit(0);
            }
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: " + solver.getUnique());
            if (path.get(0) == null) {
                System.out.println("Step " + num + ": " + water);
            } else {
                for (Configuration steps : path) {
                    System.out.println("Step " + num + ": " + steps);
                    num++;
                }
            }
        }
    }
}
