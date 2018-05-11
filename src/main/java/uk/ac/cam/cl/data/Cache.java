package uk.ac.cam.cl.data;

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
 * @author Nathan Corbyn
 */
public class Cache {
    private Clock clock;
    
    private Path cache;
    private JSONObject data;

    private boolean lock = false;
    private int threadCount = 0; 

    /**
     * Initialise the cache using a given clock and cache file (note
     * that this causes a read from the given file in all cases other 
     * than when the cache file does not exist)
     * @param clock clock to use for timestamping
     * @param cache the path to the cache file to be used
     * @throws IOException when read fails and a cache could not be created
     */
    public Cache(Clock clock, Path cache) throws IOException {
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
            String json = "", line;
            while ((line = reader.readLine()) != null)
                json += line;
            JSONParser parser = new JSONParser();
            JSONObject temp = (JSONObject) parser.parse(json);

            if (!temp.containsKey("marine") 
                    || !temp.containsKey("local") 
                    || !temp.containsKey("cache_timestamp"))
                throw new IOException("Malformed cache");
            else data = temp;
        } catch (IOException e) {
            //Load failed so create new cache file
            cache.toFile().createNewFile();  
        } catch (ParseException e) {
            //Parse failed (i.e. corrupt cache so delete cache and reform)
            cache.toFile().delete();
            cache.toFile().createNewFile();  
        } finally { reader.close(); }
        
        return data;
    }

    /**
     * Write current cache data to disk
     * @throws IOException if write cache failed
     */
    private void write() throws IOException {
        String out = data.toJSONString();
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
        dump(); 
    }

    /**
     * Attempts to dump the cache to disk (asynchronous)
     */
    public void dump() {
        if (threadCount < 2) {
            threadCount++;
            (new Thread(() -> {
                //Spin lock (prevents concurrent file access) 
                while (lock) {
                    try { Thread.sleep(100); } 
                    catch (InterruptedException e) { 
                        threadCount--; 
                        return; 
                    }
                }

                lock = true;      //Grab lock
                try { write(); }  //Attempt write back
                catch (IOException e) {
                    e.printStackTrace();    
                } finally {
                    lock = false; //Release lock
                    threadCount--;
                }
            })).start();
        }
    }

    /**
     * Get currently cached data
     */
    public JSONObject getData() {
        return data;
    }
}

