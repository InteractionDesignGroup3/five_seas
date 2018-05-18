package uk.ac.cam.cl.data;

/**
 * Represents a single location
 *
 * @author Nathan Corbyn
 */
public class Location {
  private final String name;
  private final double longitude, latitude;

  /**
   * Intialises a new location with the given name, longitude and latitude
   *
   * @param name the name of the location
   * @param longitude the longitude of the location
   * @param latitude the latitude of the location
   */
  public Location(String name, double longitude, double latitude) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public String getName() {
    return name;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public String toString() {
    return name + " (long. " + longitude + ", lat. " + latitude + ")";
  }
}
