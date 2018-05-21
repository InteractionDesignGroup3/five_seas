package uk.ac.cam.cl.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class for reading from simple JSON configuration files
 *
 * @author Nathan Corbyn, Max Campman
 */
public class Config {
  private JSONObject configuration;

  /**
   * Create a new configuration object from a given input stream
   *
   * @param path the filepath of the configuration (internal to the jar)
   * @throws ConfigurationException if the file cannot be read or parsed
   */
  public Config(String path) throws ConfigurationException {
    try {
      Charset charset = Charset.forName("UTF-8");
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
      StringBuilder json = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) json.append(line);
      JSONParser parser = new JSONParser();
      configuration = (JSONObject) parser.parse(json.toString());
    } catch (IOException e) {
      throw new ConfigurationException(
          "Configuration file " + path + " could not be read");
    } catch (ParseException e) {
      throw new ConfigurationException(
          "Configuration file " + path + " was not valid JSON");
    }
  }

  /**
   * Get an object from the loaded configuration
   *
   * @param key the key for the object object
   * @return the corresponding value
   * @throws ConfigurationException if the key is not found
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key) throws ConfigurationException {
    try {
      T value = (T) configuration.get(key);
      if (value != null) return value;
      else throw new ConfigurationException("No value for key " + key + " found");
    } catch (ClassCastException e) {
      throw new ConfigurationException("Type mismatch for key " + key);
    }
  }
}
