package uk.ac.cam.cl.data;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Provides all JSON data requried by the data manager (this
 * may either be from the API directly or from a cache)
 * @author Nathan Corbyn, Max Campman
 */
public class APIConnector {
    private final String apiURL, requestBody, user, password;
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
            apiURL = (String) config.get("api_url");

            StringBuilder requestBuilder = new StringBuilder();
            requestBuilder.append("ZP");
            requestBuilder.append((String) config.get("api_period"));
            requestBuilder.append(":PT");
            requestBuilder.append((String) config.get("api_interval"));
            requestBuilder.append("/");

            JSONArray parameters = (JSONArray) config.get("api_parameters");
            for (int i = 0; i < parameters.size(); i++) {
                requestBuilder.append((String) parameters.get(i));
                if (i < parameters.size() - 1)
                    requestBuilder.append(",");
                else requestBuilder.append("/");
            }

            requestBody = requestBuilder.toString();
            user = (String) config.get("api_user");
            password = (String) config.get("api_password");
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
            apiURL = (String) config.get("api_url");

            StringBuilder requestBuilder = new StringBuilder();
            requestBuilder.append("P");
            requestBuilder.append((String) config.get("api_interval"));
            requestBuilder.append(":PT");
            requestBuilder.append((String) config.get("api_period"));
            requestBuilder.append("/");

            JSONArray parameters = (JSONArray) config.get("api_parameters");
            for (int i = 0; i < parameters.size(); i++) {
                requestBuilder.append((String) parameters.get(i));
                if (i < parameters.size() - 1)
                    requestBuilder.append(",");
                else requestBuilder.append("/");
            }

            requestBody = requestBuilder.toString();
            user = (String) config.get("api_user");
            password = (String) config.get("api_password");
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
    public APIConnector(String apiURL, 
            String requestBody, 
            String user, 
            String password, 
            Cache cache) {
        this.apiURL = apiURL;
        this.requestBody = requestBody;
        this.user = user;
        this.password = password;
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

        try {
            DateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            String time = format.format(new Date()) + "T00:00:00Z/"; 
            HttpRequest request = HttpRequest.get("https://" +
                    user + ":" +
                    password + "@" +
                    apiURL + 
                    time + 
                    requestBody +
                    latitude + "," + longitude +
                    "/json?model=mix", true); 
            System.out.println(request.toString());
            
            if (request.ok()) { 
                String response = request.body();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject data = (JSONObject) parser.parse(response);
                    temp.put("dump", data);
                } catch (ParseException e) { e.printStackTrace(); }
            }
        } catch (HttpRequest.HttpRequestException e) { e.printStackTrace(); }

        if (temp.containsKey("dump"))
            cache.update(temp);
        return cache.getData(); //Always return from cache for timestamp
    }
}

