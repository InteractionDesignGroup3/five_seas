package uk.ac.cam.cl.data;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Clock;
import org.json.simple.JSONObject;

/**
 * Manages all settings used throughout the application
 *
 * @author Nathan Corbyn
 */
public class AppSettings {
  private static AppSettings instance;
  private Cache settings;
  private static final String SETTINGS_FILE = "settings.json";

  /**
   * Singleton constructor intialises cache (could throw an AppSettingsFailure but please do not
   * catch this)
   */
  private AppSettings() throws IOException {
    settings = new Cache(Clock.systemUTC(), Paths.get(SETTINGS_FILE));
  }

  /**
   * Get the app settings instance
   *
   * @return the app settings instance
   * @throws AppSettingsFailure in the event the cache could not be initialised
   */
  public static AppSettings getInstance() {
    try {
      if (instance == null) instance = new AppSettings();
      return instance;
    } catch (IOException e) {
      throw new AppSettingsFailure(e.getMessage());
    }
  }

  /**
   * Gets the setting with the given key
   *
   * @param key the key for the desired value
   * @throws ClassCastException if the value is not of the desired type
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    return (T) settings.getData().get(key);
  }

  /**
   * Gets the setting with the given key (if the key is not found, sets the setting to the default
   * value and returns the default value)
   *
   * @param key the key for the desired value
   * @param t the default setting value
   * @throws ClassCastException if the value is not of the desired type
   */
  @SuppressWarnings("unchecked")
  public <T> T getOrDefault(String key, T t) {
    T value = (T) settings.getData().get(key);
    if (value == null) {
      this.set(key, t);
      return t;
    } else return value;
  }

  /**
   * Updates the setting with the given key to the given value
   *
   * @param key the key to update
   * @param value the value to update it to
   */
  @SuppressWarnings("unchecked")
  public <T> void set(String key, T value) {
    JSONObject newSettings = settings.getData();
    newSettings.put(key, value);
    settings.update(newSettings);
  }
}
