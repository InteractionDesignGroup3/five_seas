package uk.ac.cam.cl.data;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javafx.application.Platform;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ac.cam.cl.data.apis.APIRequestException;
import uk.ac.cam.cl.data.apis.HereMaps;
import uk.ac.cam.cl.data.apis.Meteomatics;
import uk.ac.cam.cl.data.apis.WorldWeatherOnline;

/**
 * Manages all data presented to the application (this includes 
 * extracting data points from API responses)
 * @author Nathan Corbyn
 */
public class DataManager {
    public static final long UPDATE_INTERVAL = 900000; 
    public static final String WEATHER_CONFIG = "config.json",
           LOCATION_CONFIG = "here.json";

    private static DataManager instance;
    private Thread daemon;
    private APIConnector<DataSequence> api; 
    private APIConnector<Location> locationService;

    private int day = 0;
    private long lastUpdated = 0;
    private double longitude, latitude;

    private List<DataSequence> data = new ArrayList<>();
    private List<Consumer<List<DataSequence>>> listeners = new ArrayList<>();

    private static final double COORD_ERROR = 9.0e-5;

    /**
     * Singleton constructor initialises daemon thread (could
     * throw an APIFailure but please do not catch this)
     */
    private DataManager() {
        api = new APIConnector<DataSequence>(new Meteomatics(), 
                Paths.get(WEATHER_CONFIG));
        locationService = new APIConnector<Location>(new HereMaps(),
                Paths.get(LOCATION_CONFIG));
        
        try { 
            JSONObject apiData = api.getData();
            data = api.getProcessedData(apiData);
            longitude = (Double) apiData.get("longitude");
            latitude = (Double) apiData.get("latitude");
            lastUpdated = (Long) apiData.get("cache_timestamp");
        } catch (NoDataException | APIRequestException e) {
            //TODO cache is fresh so generate current location (either
            //lock this to Cambridge in the config or fake it with IP -
            //currently locked to Cambridge)
            longitude = 0.1218;
            latitude = 52.2053;
        }

        daemon = new Thread(() -> {
            while (true) {
                update();
                try { Thread.sleep(UPDATE_INTERVAL); } 
                catch (InterruptedException e) { continue; }
                //Thread interrupted means update now so skip to next iteration
            }
        });

        daemon.setDaemon(true); //Automatically kill thread on program termination
        daemon.start();
    }
  
    /**
     * Get the data manager instance
     * @return the data manager singleton
     * @throws APIFailure in the event the API connector could not be started
     */
    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    /**
     * Updates the available data and triggers all listeners
     */
    private void update() {
        while (true) {
            JSONObject apiData = api.getData(longitude, latitude);
            double apiLongitude = (Double) apiData.get("longitude");
            double apiLatitude = (Double) apiData.get("latitude");

            //If API does not return data for target use cache coordinates
            if (Math.abs(apiLongitude - longitude) < COORD_ERROR)
                longitude = apiLongitude;
            if (Math.abs(apiLatitude - latitude) < COORD_ERROR)
                latitude = apiLatitude;

            try {
                data = new ArrayList<>(api.getProcessedData(apiData));
                Collections.sort(data);
                triggerAll(); 
                break;
            } catch (APIRequestException | NullPointerException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(500);
                    continue; 
                } catch (InterruptedException e2) { continue; }
            }
        }
    }

    /**
     * Returns a list of suggested locations based on the target string
     * @param target the target location around which to search
     * @return a list of processed locations
     */
    public List<Location> getLocations(String target) {
        try {
            JSONObject locationData = 
                locationService.getData(longitude, latitude, target);
            return locationService.getProcessedData(locationData);
        } catch (APIRequestException e) {
            return new ArrayList<>(); 
        }
    }
    
    /**
     * Get the longitude coordinate the data currently regards
     * @return current longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the latitude coordinate the data currently regards
     * @return current latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the timestamp of the last update
     * @return when the available data was fetched from the API
     */
    public long getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Update the coordinates currently pointed to (this will 
     * automatically trigger the daemon to update its data)
     * @param longitude the longitude coordinate
     * @param latitude the latitude coordinate
     */
    public void setCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        daemon.interrupt(); //Force update
    }

    /**
     * Add a listener (called when data is updated)
     * @param listener consumer executed on data change
     */
    public void addListener(Consumer<List<DataSequence>> listener) {
        listeners.add(listener);
        listener.accept(data);
    }

    /**
     * Triggers all attached listeners
     */
    public void triggerAll() {
        Platform.runLater(() -> {
            listeners.forEach(listener -> listener.accept(data));
        });
    }

    /**
     * Sets the current day to the given value [0, 6] and calls all
     * listeners
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
     * @return the currently selected day
     */
    public int getDay() {
        return day;
    }
}

