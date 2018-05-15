package uk.ac.cam.cl.data;

import java.util.Set;
import java.util.HashSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Enables JSON to be read and written to cache; also manages
 * threading of caching write backs
 * @author Nathan Corbyn, Max Campman
 */
public class Cache {
    private Clock clock;
    
    private Path cache;
    private JSONObject data;

    private boolean lock = false, created = false;
    private int threadCount = 0;

    private static Set<Path> usedFiles = new HashSet<>();

    /**
     * Initialise the cache using a given clock and cache file (note
     * that this causes a read from the given file in all cases other 
     * than when the cache file does not exist)
     * @param clock clock to use for timestamping
     * @param cache the path to the cache file to be used
     * @throws IOException when read fails and a cache could not be created
     */
    public Cache(Clock clock, Path cache) throws IOException {
        if (usedFiles.contains(cache)) {
            throw new APIFailure("Cannot have multiple cache instances writing to the same file: " + cache);
        }
        this.clock = clock;
        this.cache = cache;
        data = read(); 
    }
    
    /**
     * Read cache data from disk
     * @return parsed JSON object from disk
     * @throws IOException if cache read failed
     */
    private JSONObject read() throws IOException {
        JSONObject data = new JSONObject();
        BufferedReader reader = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            reader = Files.newBufferedReader(cache, charset);
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                json.append(line);
            reader.close();
            
            JSONParser parser = new JSONParser();
            JSONObject temp = (JSONObject) parser.parse(json.toString());

            if (!temp.containsKey("dump") 
                    || !temp.containsKey("cache_timestamp")
                    || !temp.containsKey("longitude")
                    || !temp.containsKey("latitude"))
                throw new IOException("Malformed cache");
            else data = temp;
            reader.close();
        } catch (IOException e) {
            //Load failed so create new cache file
            cache.toFile().delete();
            cache.toFile().createNewFile();
            created = true; 
        } catch (ParseException e) {
            //Parse failed (i.e. corrupt cache so delete cache and reform)
            cache.toFile().delete();
            cache.toFile().createNewFile();  
            created = true; 
        }
        
        return data;
    }

    /**
     * Write current cache data to disk
     * @throws IOException if write cache failed
     */
    private void write() throws IOException {
        String out = this.data.toJSONString();
        BufferedWriter writer = null;
        writer = Files.newBufferedWriter(cache);
        writer.write(out);
        writer.close();
    }

    /**
     * Updates cached JSON to given JSON object
     * @param data the JSON object to update the cache to
     */
    @SuppressWarnings("unchecked")
    public void update(JSONObject data) {
        this.data = new JSONObject(data);
        this.data.put("cache_timestamp", new Long(clock.millis()));
        this.created = false;
        dump(); 
    }

    /**
     * Attempts to dump the cache to disk (asynchronous)
     */
    public void dump() {
        if (this.threadCount < 2) {
            this.threadCount++;
            (new Thread(() -> {
                //Spin lock (prevents concurrent file access) 
                while (this.lock) {
                    try { Thread.sleep(100); } 
                    catch (InterruptedException e) { 
                        this.threadCount--;
                        return; 
                    }
                }

                this.lock = true;      //Grab lock
                try { write(); }  //Attempt write back
                catch (IOException e) {
                    e.printStackTrace();    
                } finally {
                    this.lock = false; //Release lock
                    this.threadCount--;
                }
            })).start();
        }
    }

    /**
     * Get currently cached data
     * @return the currently cached data
     */
    public JSONObject getData() {
        return this.data;
    }

    /**
     * Was the cache just created
     * @return true if the cache was just created (false otherwise)
     */
    public boolean isNew() {
        return this.created;
    }

    /**
     * Remove the cache from the set of files used
     */
    public void closeCache() {
        Path tmp = cache;
        cache = null;
        usedFiles.remove(tmp);
    }
}

