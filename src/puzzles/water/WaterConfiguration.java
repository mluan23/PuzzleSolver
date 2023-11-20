package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The representation for a single WaterConfiguration for the water buckets puzzle.
 */

public class WaterConfiguration implements Configuration {
    /** the neighboring WaterConfigurations for a given WaterConfiguration */
    private List<Configuration> neighbors;
    /** the buckets in a given WaterConfiguration */
    private List<Bucket> buckets;
    /** used for creating a copy of the buckets */
    private List<Bucket> original;
    /** the amount of water that is trying to be reached in any bucket */
    private int goal;

    /**
     * Creates a WaterConfiguration object. Used for creating the starting Configuration of the puzzle,
     * and for generating neighbors.
     * @param goal the amount of water trying to be reached
     * @param bucketList a list of Buckets to represent the buckets
     */
    public WaterConfiguration(int goal, List<Bucket> bucketList){
        neighbors = new ArrayList<>();
        this.goal = goal;
        this.buckets = new ArrayList<>(bucketList);
    }

    /**
     * Checks if the given WaterConfiguration is a solution by checking if the current amount of water in any bucket
     * matches the goal.
     * @return true if it is a solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        for (Bucket bucket : buckets){
            if (bucket.getCurrent() == goal){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the neighbors of a given WaterConfiguration. The neighbors are obtained by:
     * 1. Filling a bucket to its maximum - only for buckets that are not full
     * 2. Dumping a bucket, making its water amount 0 - only for buckets that are not empty
     * 3. Pouring from one bucket to another - only from buckets that are not empty, to buckets that are not full
     * @return an ArrayList of the neighbors
     */

    @Override
    public Collection<Configuration> getNeighbors() {
        for (int i = 0; i<buckets.size(); i++){ // filling option
            original = new ArrayList<>(this.buckets);
            if (buckets.get(i).getCurrent() < buckets.get(i).getMax()){
                buckets.set(i, new Bucket(buckets.get(i).getMax(), buckets.get(i).getMax()));
            }
            else if(buckets.get(i).getCurrent() > 0){ // dumping option
                buckets.set(i, new Bucket(0, buckets.get(i).getMax()));
            }
            neighbors.add(new WaterConfiguration(goal, buckets));
            buckets = original;
            if (buckets.get(i).getCurrent() > 0) { // pouring option
                for (int k = 0; k < buckets.size(); k++) {
                    if (buckets.get(k).getCurrent() < buckets.get(k).getMax() && k != i) {
                        //only pour into buckets that are not the same, and are not filled
                        original = new ArrayList<>(this.buckets);
                        int amount = Math.min
                                (buckets.get(k).getMax() - buckets.get(k).getCurrent(), buckets.get(i).getCurrent());
                        // pour until one bucket is full, or one bucket is empty
                        buckets.set
                                (i, new Bucket(buckets.get(i).getCurrent() - amount, buckets.get(i).getMax()));
                        buckets.set
                                (k, new Bucket(buckets.get(k).getCurrent() + amount, buckets.get(k).getMax()));
                        neighbors.add(new WaterConfiguration(goal, buckets));
                        buckets = original;
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Two WaterConfigurations are equal if they contain the same Buckets.
     * @param other the other WaterConfiguration
     * @return whether the WaterConfigurations are equal or not
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof WaterConfiguration){
            WaterConfiguration config = (WaterConfiguration)other;
            return this.buckets.equals(config.buckets);
        }
        return false;
    }

    /**
     * The hash code of each WaterConfiguration is equal to the hash code of each Bucket
     * multiplied, based on the positions of the Buckets.
     * @return the hash code of the WaterConfiguration
     */
    @Override
    public int hashCode(){
        int s = 1;
        int position = -1;
        for (Bucket bucket : buckets) {
            position++;
            s *= (position + bucket.hashCode());
        }
        return s;
    }

    /**
     * Represents each WaterConfiguration as a list of Buckets.
     * @return the toString of each individual bucket
     */
    @Override
    public String toString(){
        return buckets.toString();
    }
}
