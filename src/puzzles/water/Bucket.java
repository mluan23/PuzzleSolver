package puzzles.water;

/**
 * The class used for representing individual Buckets for WaterConfigurations.
 */

public class Bucket {
    /** the current amount of water in the bucket */
    private int current;
    /** the maximum amount of water the bucket can hold */
    private int max;

    /**
     * Creates a Bucket object. The current amount cannot exceed the max amount of water.
     * @param current the current amount of water in the bucket
     * @param max the max amount of water the bucket can hold
     */
    public Bucket(int current, int max){
        this.current = current;
        this.max = max;
        if (this.current > max){
            this.current = max;
        }
    }

    /**
     * Gives the current amount of water in the bucket.
     * @return current
     */
    public int getCurrent(){
        return current;
    }

    /**
     * Gives the maximum amount of water the bucket can hold.
     * @return max
     */
    public int getMax(){
        return max;
    }

    /**
     * Two Buckets are equal if they have the same amount of water, and if they can hold the
     * same amount of water.
     * @param other the other Bucket
     * @return true if the Buckets are equal, false otherwise
     */

    @Override
    public boolean equals(Object other){
        if (other instanceof Bucket){
            Bucket b = (Bucket)other;
            return this.current == b.current && this.max == b.max;
        }
        return false;
    }

    /**
     * The hash code of each bucket is equal to its maximum + current amount of water.
     * @return max + current
     */
    @Override
    public int hashCode(){
        return max + current;
    }
    /**
     * Represents each bucket as its current amount of water.
     * @return the String value of current
     */
    @Override
    public String toString(){
        return String.valueOf(current);
    }
}
