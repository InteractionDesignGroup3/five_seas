package uk.ac.cam.cl.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class for reading from simple JSON configuration files.
 * @author Nathan Corbyn, Max Campman
 */
public class Config {
    private JSONObject configuration;

    /**
     * Create a new configuration object from a given filepath.
     * @param path the filepath of the configuration
     * @throws ConfigurationException if the file cannot be read or parsed
     */
    public Config(Path path) throws ConfigurationException {
        try {
            Charset charset = Charset.forName("UTF-8");
            BufferedReader reader = Files.newBufferedReader(path, charset);
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                json.append(line);
            JSONParser parser = new JSONParser();
            configuration = (JSONObject) parser.parse(json.toString());
        } catch (IOException e) {
            throw new ConfigurationException("Configuration file " + path.getFileName() + " could not be read");        
        } catch (ParseException e) {
            throw new ConfigurationException("Configuration file " + path.getFileName() + " was not valid JSON");        
        }
    }

    /**
     * Get an object from the loaded configuration.
     * @param key the key for the object object
     * @return the corresponding value 
     * @throws ConfigurationException if the key is not found
     */
    public Object get(String key) throws ConfigurationException {
        Object value = configuration.get(key);
        if (value != null) return value;
        else throw new ConfigurationException("No value for key " + key + " found");
    }
}

