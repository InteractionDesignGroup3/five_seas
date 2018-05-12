package uk.ac.cam.cl.data;

public class DataManager {
    private static DataManager instance;
    private Thread daemon;

    private DataManager() {
        //TODO
        daemon = new Thread(() -> {
            
        });
    }
    
    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    public void update() {

    }
}

