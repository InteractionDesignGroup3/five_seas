package uk.ac.cam.cl.data;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ac.cam.cl.data.apis.APIRequestException;
import uk.ac.cam.cl.data.apis.Meteomatics;
import uk.ac.cam.cl.data.apis.WorldWeatherOnline;

/**
 * Manages all data presented to the application (this includes 
 * extracting data points from API responses)
 * @author Nathan Corbyn
 */
public class DataManager {
    public static final long UPDATE_INTERVAL = 900000; 
    public static final String CONFIG = "wwo.json";

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
        //TODO switch to Meteomatics
        api = new APIConnector(new WorldWeatherOnline(), Paths.get(CONFIG));
        
        try { 
            JSONObject apiData = api.getData();
            longitude = (Double) apiData.get("longitude");
            latitude = (Double) apiData.get("latitude");
            lastUpdated = (Long) apiData.get("cache_timestamp");
        } catch (NoDataException e) {
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
        JSONObject apiData = api.getData(longitude, latitude);
        //TODO check cache long and lat match actual long and lat
        try {
            dataSequence = new ArrayList<>(api.getProcessedData(apiData));
            listeners.forEach(listener -> listener.accept(dataSequence));
        } catch (APIRequestException e) {
            //TODO improve failure pathway
            e.printStackTrace();
        } catch(NullPointerException e) {
            //API response is empty so may as well try again
            update(); 
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
        listener.accept(dataSequence);
    }
}

