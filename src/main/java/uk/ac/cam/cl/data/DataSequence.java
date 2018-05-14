package uk.ac.cam.cl.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.cam.cl.data.DataPoint;

/**
 * Represents a single day's data (essentially a list of data
 * points plus a number of parameters specific to the day)
 * @author Nathan Corbyn
 */
public class DataSequence implements Comparable<DataSequence> {
    private final List<DataPoint> sequence;
    private final long time;
    private final double maxTemperature,  //Max temperature in degrees C
            minTemperature;               //Min temperature in degrees C

    /**
     * Initialises a new data sequence with the given start time
     * and minimum and maximum temperatures and list of data points
     * @param time time corresponding to data (unix)
     * @param maxTemperature the maximum temperature over the sequence
     * @param minTemperature the minimum temperature over the sequence
     */
    public DataSequence(long time, 
            double maxTemperature, 
            double minTemperature,
            List<DataPoint> sequence) {
        this.time = time;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        Collections.sort(sequence);
        this.sequence = sequence;
    }

    /**
     * Orders data points by their time stamp
     * @param other the data point to compare to
     * @return an integer representing the computed ordering
     */
    @Override
    public int compareTo(DataSequence other) {
        return (new Long(time)).compareTo(new Long(other.time));
    }

    public long getTime() {
        return time;
    }

    public double getMaxTemperatureC() {
        return maxTemperature;
    }
    
    public double getMaxTemperatureF() {
        return Conversions.celciusToFarenheit(maxTemperature);
    }

    public double getMinTemperatureC() {
        return minTemperature;
    }

    public double getMinTemperatureF() {
        return Conversions.celciusToFarenheit(minTemperature);
    }

    /**
     * Get the size of the sequence
     * @return the size of the sequence
     */
    public int size() {
        return sequence.size();
    }

    /**
     * Get the nth element in the sequence
     * @param n the index of the element to get
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if an invalid index is provided
     */
    public DataPoint get(int n) {
        return sequence.get(n);
    }

    @Override
    public String toString() {
        return sequence.toString();
    }
}

