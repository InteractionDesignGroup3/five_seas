package uk.ac.cam.cl.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.List;
import org.json.simple.JSONObject;
import uk.ac.cam.cl.data.apis.API;
import uk.ac.cam.cl.data.apis.APIRequestException;

/**
 * Provides all JSON data requried by the data manager (this may either be from the API directly or
 * from a cache)
 *
 * @author Nathan Corbyn, Max Campman
 */
public class APIConnector<T> {
  private API<T> api;
  private boolean disableRequests;
  private Cache cache;

  /**
   * Initialise connector for a given API from a given configuration file
   *
   * @param api the API to use
   * @param path path to the configuration file
   * @throws APIFailure in the event of a non-recoverable failure mode
   */
  public APIConnector(API<T> api, String path) {
    this.api = api;
    try {
      Config config = new Config(path);
      api.initFromConfig(config);
      disableRequests = (Boolean) config.get("disable_requests");
      cache = new Cache(Clock.systemUTC(), Paths.get((String) config.get("cache")));
    } catch (ConfigurationException | IOException e) {
      e.printStackTrace();
      throw new APIFailure("Could not create or load cache");
    }
  }

  /**
   * Initialise connector for a given API from a given configuration instance
   *
   * @param api the API to use
   * @param config the configuration instance
   * @throws APIFailure in the event of a non-recoverable failure mode
   */
  public APIConnector(API<T> api, Config config) {
    this.api = api;
    try {
      api.initFromConfig(config);
      disableRequests = (Boolean) config.get("disable_requests");
      cache = new Cache(Clock.systemUTC(), Paths.get((String) config.get("cache")));
    } catch (ConfigurationException | NullPointerException | IOException e) {
      e.printStackTrace();
      throw new APIFailure("Could not create or load cache");
    }
  }

  /**
   * Initialise connector for a given API from a give configuration file ignoring any cache settings
   * and using the given cache instance
   *
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
   * Makes a request to the API for fresh data based on the longitude and latitude previously stored
   * in the cache
   *
   * @return parsed JSON object from API or cache
   * @throws NoDataException if the cache is newly formed
   */
  public JSONObject getData() throws NoDataException {
    if (cache.isNew()) throw new NoDataException();
    else {
      double longitude = (Double) cache.getData().get("longitude");
      double latitude = (Double) cache.getData().get("latitude");
      String name = (String) cache.getData().get("place_name");
      return getData(new Location(name, longitude, latitude));
    }
  }

  /**
   * Makes a request to the API for fresh data for the given location (if this can't be found or is
   * somehow malformed, previously cached data is returned)
   *
   * @param location target location
   * @return parsed JSON object from API or cache
   */
  @SuppressWarnings("unchecked")
  public JSONObject getData(Location location) {
    if (disableRequests) return cache.getData();
    if (location.getLatitude() > 180
        || location.getLatitude() < 0
        || location.getLongitude() > 180
        || location.getLatitude() < -180) return cache.getData();
    JSONObject temp = new JSONObject();
    temp.put("longitude", location.getLongitude());
    temp.put("latitude", location.getLatitude());
    temp.put("place_name", location.getName());

    try {
      JSONObject apiResponse = api.getData(location);
      temp.put("dump", apiResponse);
      cache.update(temp);
      return cache.getData();
    } catch (APIRequestException e) {
      return cache.getData();
    }
  }

  /**
   * Uses API implementation to produce data sequences
   *
   * @param data data to convert to data sequence
   * @return processed data sequences
   * @throws APIRequestException if the API data could not be processed
   */
  public List<T> getProcessedData(JSONObject data) throws APIRequestException {
    try {
      return api.getProcessedData(data);
    } catch (NullPointerException e) {
      return api.getProcessedData(cache.getData());
    }
  }

  /** Disables API requests */
  public void disableRequests() {
    disableRequests = true;
  }

  /** Enables API requests */
  public void enableRequests() {
    disableRequests = false;
  }
}
