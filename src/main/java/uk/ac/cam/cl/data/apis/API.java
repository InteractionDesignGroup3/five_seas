package uk.ac.cam.cl.data.apis;

import java.util.List;

import org.json.simple.JSONObject;

import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataSequence;
import uk.ac.cam.cl.data.Location;

/**
 * Standard interface implemented by all APIs 
 * @author Nathan Corbyn
 */
public interface API<T> {
    /**
     * Initialises the API connector from the given configuration instance
     * @param config the configuration instance to initialise from
     * @throws ConfigurationException if the configuration could not be read
     */
    public void initFromConfig(Config config) throws ConfigurationException;

    /**
     * Returns data processed as per the format of the API such that
     * it can be used by the data manager
     * @param data the data to be processed
     * @return the processed API response
     * @throws APIRequestException if the API data could not be processed
     */
    public List<T> getProcessedData(JSONObject data)
            throws APIRequestException;

    /**
     * Makes a request to the API for fresh data for the given location
     * @param longitude target longitude
     * @param latitude target latitude
     * @param target the target location
     * @return parsed JSON object from API
     * @throws APIRequestException if API data could not be fetched
     */
    public JSONObject getData(Location location) 
            throws APIRequestException;
}

