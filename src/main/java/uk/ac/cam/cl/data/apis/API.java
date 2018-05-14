package uk.ac.cam.cl.data.apis;

import java.util.List;

import org.json.simple.JSONObject;

import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataSequence;

/**
 * Standard interface implemented by all APIs 
 * @author Nathan Corbyn
 */
public interface API {
    /**
     * Initialises the API connector from the given configuration instance
     * @param config the configuration instance to initialise from
     * @throws ConfigurationException if the configuration could not be read
     */
    public void initFromConfig(Config config) throws ConfigurationException;

    /**
     * Returns data processed as per the format of the API such that
     * it can be used by the data manager
     * @return the processed API response
     * @throws APIRequestException if the API data could not be processed
     */
    public List<DataSequence> getProcessedData(JSONObject data)
            throws APIRequestException;

    /**
     * Makes a request to the API for fresh data for the given longitude 
     * and latitude 
     * @param longitude target longitude
     * @param latitude target latitude
     * @return parsed JSON object from API
     * @throws APIRequestException if API data could not be fetched
     */
    public JSONObject getData(double longitude, double latitude)
            throws APIRequestException;
}

