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
 * @author Nathan Corbyn
 */
public class APIConnector {
    private final String api, token, marine, local;
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
            cache = new Cache(Clock.systemUTC(), 
                    Paths.get((String) config.get("cache")));
        } catch(ConfigurationException e) {
            //Non-recoverable failure mode
            e.printStackTrace();
            throw new APIFailure("Could not load configuration " + path.getFileName()); 
        } catch (IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
        }
    }

    /**
     * Initialise connector from an API location, token and cache file directly
     * @param location the API URL
     * @param token API token
     * @param cache path to the cache file
     */
    public APIConnector(String api, String marine, String local, String token, Path cache) {
        this.api = api;
        this.marine = marine;
        this.local = local;
        this.token = token;
        try { this.cache = new Cache(Clock.systemUTC(), cache); }
        catch (IOException e) {
            e.printStackTrace();
            throw new APIFailure("Could not create or load cache");
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
        JSONObject temp = new JSONObject();

        //Local request
        HttpRequest localRequest = HttpRequest.get(api + local, true,
                'q', latitude + "," + longitude, //Set query location 
                "num_of_days", 7, //Whole weeks forecast
                "date", "today",  //From today
                "fx", "yes",      //Specify whether to include weather forecast
                "format", "json", //Specify format (default is XML)
                "key", token,     //Pass API token
                "tp", 1,          //Sets the time period for requests (hours)
                "includelocation", "yes"); //Show location 
        
        if (localRequest.ok()) { 
            String response = localRequest.body();
            System.out.println(response);
            try {
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(response);
                temp.put("local", data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
            System.out.println(response);
            try {
                JSONParser parser = new JSONParser();
                JSONObject data = (JSONObject) parser.parse(response);
                temp.put("marine", data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (temp.containsKey("marine") && temp.containsKey("local"))
            cache.update(temp);
        return cache.getData(); //Always return from cache for timestamp
    }
}

