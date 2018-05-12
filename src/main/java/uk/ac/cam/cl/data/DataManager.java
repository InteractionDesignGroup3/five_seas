package uk.ac.cam.cl.data;

import java.nio.file.Paths;

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
     */
    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

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
    }

    /**
     * Force the data manager to update data immediately
     */
    public void forceUpdate() {
        daemon.interrupt();
    }
}

