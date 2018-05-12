package uk.ac.cam.cl.data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.json.simple.JSONObject;

/**
 * Manages all data presented to the application (this includes 
 * extracting data points from API responses)
 * @author Nathan Corbyn
 */
public class DataManager {
    public static final long UPDATE_INTERVAL = 900000; 
    public static final String CONFIG = "config.json";

    private static DataManager instance;
    private Thread daemon;
    private APIConnector api; 

    private long lastUpdated = 0;
    private double longitude, latitude;

    private List<DataPoint> dataSequence = new ArrayList<>();
    private List<Consumer<List<DataPoint>>> listeners = new ArrayList<>();

    /**
     * Singleton constructor initialises daemon thread (could
     * throw an APIFailure but please do not catch this)
     */
    private DataManager() {
        api = new APIConnector(Paths.get(CONFIG));
        daemon = new Thread(() -> {
            while (true) {
                System.out.println("Updating data");
                update();
                try {
                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    //Thread interrupted means update now so skip to next iteration
                    continue;
                }
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
        JSONObject data = null;
        try { 
            data = api.getData();
            longitude = (Double) data.get("longitude");
            latitude = (Double) data.get("latitude");
            lastUpdated = (Long) data.get("cache_timestamp");
        } catch (NoDataException e) {
            //TODO cache is fresh so generate current location
            longitude = 0;
            latitude = 0;
            data = api.getData(longitude, latitude);
        }

        //TODO process data
       
        //Trigger listeners
        listeners.forEach(listener -> listener.accept(dataSequence));
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
    public void addListener(Consumer<List<DataPoint>> listener) {
        listeners.add(listener);
    }
}

