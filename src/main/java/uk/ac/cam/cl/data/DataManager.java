package uk.ac.cam.cl.data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import uk.ac.cam.cl.data.apis.APIRequestException;
import uk.ac.cam.cl.data.apis.HereMaps;
import uk.ac.cam.cl.data.apis.Meteomatics;

/**
 * Manages all data presented to the application (this includes extracting data points from API
 * responses)
 *
 * @author Nathan Corbyn
 */
public class DataManager {
  public static final long UPDATE_INTERVAL = 900000;
  public static final String WEATHER_CONFIG = "config.json", LOCATION_CONFIG = "here.json";

  private static DataManager instance;
  private Thread daemon;
  private APIConnector<DataSequence> api;
  private APIConnector<Location> locationService;

  private int day = 0;
  private long lastUpdated = 0;
  private Location location;

  private List<DataSequence> data = new ArrayList<>();
  private List<Consumer<List<DataSequence>>> listeners = new ArrayList<>();

  private static final double COORD_ERROR = 9.0e-5;

  /**
   * Singleton constructor initialises daemon thread (could throw an APIFailure but please do not
   * catch this)
   */
  private DataManager() {
    api = new APIConnector<DataSequence>(new Meteomatics(), Paths.get(WEATHER_CONFIG));
    locationService = new APIConnector<Location>(new HereMaps(), Paths.get(LOCATION_CONFIG));

    try {
      JSONObject apiData = api.getData();
      data = api.getProcessedData(apiData);
      double longitude = (Double) apiData.get("longitude");
      double latitude = (Double) apiData.get("latitude");
      String name = (String) apiData.get("place_name");
      location = new Location(name, longitude, latitude);
      lastUpdated = (Long) apiData.get("cache_timestamp");
    } catch (NoDataException | APIRequestException e) {
      // TODO cache is fresh so generate current location (either
      // lock this to Cambridge in the config or fake it with IP -
      // currently locked to Cambridge)
      location = new Location("Cambridge", 0.1218, 52.2053);
    }

    daemon =
        new Thread(
            () -> {
              while (true) {
                update();
                try {
                  Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                  continue;
                }
                // Thread interrupted means update now so skip to next iteration
              }
            });

    daemon.setDaemon(true); // Automatically kill thread on program termination
    daemon.start();
  }

  /**
   * Get the data manager instance
   *
   * @return the data manager singleton
   * @throws APIFailure in the event the API connector could not be started
   */
  public static DataManager getInstance() {
    if (instance == null) instance = new DataManager();
    return instance;
  }

  /** Updates the available data and triggers all listeners */
  private void update() {
    while (true) {
      try {
        JSONObject apiData = api.getData(location);
        double longitude = (Double) apiData.get("longitude");
        double latitude = (Double) apiData.get("latitude");
        String name = (String) apiData.get("place_name");

        // If API does not return data for target use cache coordinates
        if (Math.abs(longitude - location.getLongitude()) < COORD_ERROR
            || Math.abs(latitude - location.getLatitude()) < COORD_ERROR)
          location = new Location(name, longitude, latitude);

        data = new ArrayList<>(api.getProcessedData(apiData));
        Collections.sort(data);
        triggerAll();
        break;
      } catch (APIRequestException | NullPointerException e) {
        e.printStackTrace();
        try {
          Thread.sleep(500);
          continue;
        } catch (InterruptedException e2) {
          continue;
        }
      }
    }
  }

  /**
   * Returns a list of suggested locations based on the target string
   *
   * @param target the target location around which to search
   * @return a list of processed locations
   */
  public List<Location> getLocations(String target) {
    try {
      JSONObject locationData =
          locationService.getData(
              new Location(target, location.getLongitude(), location.getLatitude()));
      return locationService.getProcessedData(locationData);
    } catch (APIRequestException e) {
      return new ArrayList<>();
    }
  }

  /**
   * Get the current location
   *
   * @return the current location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Get the timestamp of the last update
   *
   * @return when the available data was fetched from the API
   */
  public long getLastUpdated() {
    return lastUpdated;
  }

  /**
   * Update the current location of the data manager (this will automatically trigger the daemon to
   * update its data)
   *
   * @param location the current location
   */
  public void setLocation(Location location) {
    this.location = location;
    daemon.interrupt(); // Force update
  }

  /**
   * Add a listener (called when data is updated)
   *
   * @param listener consumer executed on data change
   */
  public void addListener(Consumer<List<DataSequence>> listener) {
    listeners.add(listener);
    if (data.size() > getDay()) listener.accept(data);
  }

  /** Triggers all attached listeners */
  public void triggerAll() {
    if (data.size() > getDay()) {
      Platform.runLater(
          () -> {
            listeners.forEach(listener -> listener.accept(data));
          });
    }
  }

  /**
   * Sets the current day to the given value [0, 6] and calls all listeners
   *
   * @param day the day to update the current day to
   */
  public void setDay(int day) {
    if (day < 0) this.day = 0;
    else if (day > 6) this.day = 6;
    else this.day = day;
    triggerAll();
  }

  /**
   * Returns currently selected day
   *
   * @return the currently selected day
   */
  public int getDay() {
    return day;
  }
}
