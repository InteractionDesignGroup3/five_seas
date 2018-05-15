package uk.ac.cam.cl.data;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.apis.API;
import uk.ac.cam.cl.data.apis.APIRequestException;

/**
 * Provides all JSON data requried by the data manager (this
 * may either be from the API directly or from a cache)
 * @author Nathan Corbyn, Max Campman
 */
public class APIConnector<T> {
    private API<T> api;
    private boolean disableRequests;
    private Cache cache;

    /**
     * Initialise connector for a given API from a given configuration file
     * @param api the API to use
     * @param path path to the configuration file
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(API<T> api, Path path) {
        this.api = api;
        try {
            Config config = new Config(path);
            api.initFromConfig(config);
            disableRequests = (Boolean) config.get("disable_requests");
            cache = new Cache(Clock.systemUTC(),
                    Paths.get((String) config.get("cache")));
        } catch (ConfigurationException | IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
        }
    }

    /**
     * Initialise connector for a given API from a given configuration instance
     * @param api the API to use
     * @param config the configuration instance
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(API<T> api, Config config) {
        this.api = api;
        try {
            api.initFromConfig(config);
            disableRequests = (Boolean) config.get("disable_requests");
            cache = new Cache(Clock.systemUTC(),
                    Paths.get((String) config.get("cache")));
        } catch (ConfigurationException | NullPointerException | IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
        }
    }

    /**
     * Initialise connector for a given API from a give configuration file 
     * ignoring any cache settings and using the given cache instance
     * @param api the API to use
     * @param config configuration object
     * @param cache cache object
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(API<T> api, Config config, Cache cache) {
        this(api, config);
        this.cache = cache;
    }

    /**
     * Makes a request to the API for fresh data based on the longitude and
     * latitude previously stored in the cache
     * @return parsed JSON object from API or cache
     * @throws NoDataException if the cache is newly formed
     */
    public JSONObject getData() throws NoDataException {
        if (cache.isNew()) throw new NoDataException();  
        else {
            double longitude = (Double) cache.getData().get("longitude");
            double latitude = (Double) cache.getData().get("latitude");
            return getData(longitude, latitude);
        }
    }

    /**
     * Makes a request to the API for fresh data for the given longitude 
     * and latitude (if this can't be found or is somehow malformed, 
     * previously cached data is returned)
     * @param longitude target longitude
     * @param latitude target latitude
     * @return parsed JSON object from API or cache
     */
    @SuppressWarnings("unchecked")
    public JSONObject getData(double longitude, double latitude) {
        if (disableRequests)
            return cache.getData();
        if (latitude > 180 || latitude < 0 || longitude > 180 || latitude < -180)
            return cache.getData();
        JSONObject temp = new JSONObject();
        temp.put("longitude", longitude);
        temp.put("latitude", latitude);

        try {
            JSONObject apiResponse = api.getData(longitude, latitude);
            temp.put("dump", apiResponse);
            cache.update(temp);
            return cache.getData();
        } catch (APIRequestException e) {
            return cache.getData(); 
        }
    }
    
    /**
     * Makes a request to the API for fresh data for the given coordinates 
     * and target (if this can't be found or is somehow malformed, 
     * previously cached data is returned)
     * @param longitude target longitude
     * @param latitude target latitude
     * @param target the name of the target location
     * @return parsed JSON object from API or cache
     */
    @SuppressWarnings("unchecked")
    public JSONObject getData(double longitude, double latitude, String target) {
        if (disableRequests)
            return cache.getData();
        if (latitude > 180 || latitude < 0 || longitude > 180 || latitude < -180)
            return cache.getData();
        JSONObject temp = new JSONObject();
        temp.put("longitude", longitude);
        temp.put("latitude", latitude);

        try {
            JSONObject apiResponse = api.getData(longitude, latitude);
            temp.put("dump", apiResponse);
            cache.update(temp);
            return cache.getData();
        } catch (APIRequestException e) {
            return cache.getData(); 
        }
    }

    /**
     * Uses API implementation to produce data sequences
     * @param data data to convert to data sequence
     * @return processed data sequences
     * @throws APIRequestException if the API data could not be processed
     */
    public List<T> getProcessedData(JSONObject data) 
            throws APIRequestException {
        return api.getProcessedData(data);
    }

    /**
     * Disables API requests
     */
    public void disableRequests() {
        disableRequests = true;
    }

    /**
     * Enables API requests
     */
    public void enableRequests() {
        disableRequests = false;
    }
}

