package uk.ac.cam.cl.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Provides all JSON data requried by the data manager (this
 * may either be from the API directly or from a cache)
 * @author Nathan Corbyn, Max Campman
 */
public class APIConnector {
    private final String api, token, marine, local;
    private final boolean disableRequests;
    private Cache cache;

    /**
     * Initialise connector from a given configuration file
     * @param path path to the configuration file
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(Path path) {
        try {
            Config config = new Config(path);
            api = (String) config.get("api_url");
            marine = (String) config.get("marine_api");
            local = (String) config.get("local_api");
            token = (String) config.get("api_token");
            disableRequests = (Boolean) config.get("disable_requests");
            cache = new Cache(Clock.systemUTC(),
                    Paths.get((String) config.get("cache")));
        } catch (ConfigurationException e) {
            //Non-recoverable failure mode
            e.printStackTrace();
            throw new APIFailure("Could not load config");
        } catch (IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
        }
    }

    /**
     * Initialise connector from a given configuration instance
     * @param config the configuration instance
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(Config config) {
        try {
            api = (String) config.get("api_url");
            marine = (String) config.get("marine_api");
            local = (String) config.get("local_api");
            token = (String) config.get("api_token");
            disableRequests = (Boolean) config.get("disable_requests");
            cache = new Cache(Clock.systemUTC(), 
                    Paths.get((String) config.get("cache")));
        } catch (ConfigurationException e) {
            //Non-recoverable failure mode
            e.printStackTrace();
            throw new APIFailure("Could not load config");
        } catch (IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
        }
    }

    /**
     * Initialise connector from a give configuration file ignoring any
     * cache settings and using the given cache instance
     * @param config configuration object
     * @param cache cache object
     * @throws APIFailure in the event of a non-recoverable failure mode
     */
    public APIConnector(Config config, Cache cache) {
        this(config);
        this.cache = cache;
    }

    /**
     * Initialise connector from an API url, marine API name, local API name 
     * token and cache instance
     * @param api the API URL
     * @param marine the marine API name
     * @param local the local API name
     * @param token API token
     * @param cache the cache instance 
     */
    public APIConnector(String api, 
            String marine, 
            String local, 
            String token, 
            Cache cache) {
        this.api = api;
        this.marine = marine;
        this.local = local;
        this.token = token;
        this.cache = cache;
        this.disableRequests = false;
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
     * and latitude(if this can't be found or is somehow malformed, 
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

        //Local request
        try {
            HttpRequest localRequest = HttpRequest.get(api + local, true,
                    'q', latitude + "," + longitude, //Set query location 
                    "num_of_days", 2, //Whole weeks forecast
                    "date", "today",  //From today
                    "fx", "yes",      //Specify whether to include weather forecast
                    "format", "json", //Specify format (default is XML)
                    "key", token,     //Pass API token
                    "tp", 1,          //Sets the time period for requests (hours)
                    "includelocation", "yes"); //Show location 
            System.out.println(localRequest.toString());
            
            if (localRequest.ok()) { 
                String response = localRequest.body();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject data = (JSONObject) parser.parse(response);
                    temp.put("local", data);
                } catch (ParseException e) { e.printStackTrace(); }
            }

            //Marine request
            HttpRequest marineRequest = HttpRequest.get(api + marine, true, 
                    'q', latitude + "," + longitude, //Set query location 
                    "fx", "yes",      //Specify whether to include weather forecast
                    "format", "json", //Specify format (default is XML)
                    "key", token,     //Pass API token
                    "tp", 24,         //Sets the time period for request (hours)
                    "tide", "yes");   //Include tide data (default is no)

            if (marineRequest.ok()) { 
                String response = marineRequest.body();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject data = (JSONObject) parser.parse(response);
                    temp.put("marine", data);
                } catch (ParseException e) { e.printStackTrace(); }
            }
        } catch (HttpRequest.HttpRequestException e) { e.printStackTrace(); }

        if (temp.containsKey("marine") && temp.containsKey("local"))
            cache.update(temp);
        return cache.getData(); //Always return from cache for timestamp
    }
}

