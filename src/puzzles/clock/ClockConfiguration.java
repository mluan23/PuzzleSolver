package puzzles.clock;

import puzzles.common.solver.Configuration;
import java.util.*;

/**
 * The representation for a single ClockConfiguration for the clock puzzle.
 */

public class ClockConfiguration implements Configuration {
    /** the total hours on the clock */
    private int totalHours;
    /** the current hour of the clock */
    private int current;
    /** the hour wanting to be reached */
    private int end;
    /** the neighboring ClockConfigurations */
    private List<Configuration> neighbors;

    /**
     * Creates a ClockConfiguration object. Used for creating the starting Configuration of the puzzle,
     * and for generating neighbors.
     * @param totalHours the total number of hours on the ClockConfiguration
     * @param current the current time of the ClockConfiguration
     * @param end the time trying to be reached on the ClockConfiguration
     */
    public ClockConfiguration(int totalHours, int current, int end){
        this.totalHours = totalHours;
        this.current = current;
        this.end = end;
        neighbors = new ArrayList<>();
    }

    /**
     * A solution is reached when the current time matches the goal time.
     * @return true if the ClockConfiguration is a solution, false otherwise
     */

    @Override
    public boolean isSolution() {
        return current == end;
    }

    /**
     * Gives the neighboring ClockConfigurations. The neighbors are found by:
     * 1. Subtracting one from the current hour
     * 2. Adding one to the current hour
     * @return an ArrayList of the neighbors
     */

    @Override
    public Collection<Configuration> getNeighbors() {
        int original = current;
        this.current--;
        if (current <= 0) {
            this.current = this.totalHours;
        }
        neighbors.add(new ClockConfiguration(totalHours, current, end));
        this.current = original;
        this.current ++;
        if (this.current > this.totalHours) {
            this.current = 1;
        }
        neighbors.add(new ClockConfiguration(totalHours, current, end));
        this.current = original;
        return neighbors;
    }

    /**
     * Two ClockConfigurations are equal if the current times are equal.
     * @param other the other ClockConfigurations
     * @return true if the ClockConfigurations are true, false otherwise
     */

    @Override
    public boolean equals(Object other) {
        if (other instanceof ClockConfiguration){
            ClockConfiguration config = (ClockConfiguration)other;
            return this.current == config.current;
        }
        return true;
    }

    /**
     * The hash code of a ClockConfiguration is equal to its current time.
     * @return the current time
     */
    @Override
    public int hashCode() {
        return current;
    }

    /**
     * ClockConfigurations are represented by their number of hours, their starting hour, and their goal hour.
     * @return the ClockConfiguration's total hours, starting hour, and goal hour
     */
    @Override
    public String toString(){
        return "Hours: " + totalHours + ", Start: " + current + ", End: " + end;
    }
}
