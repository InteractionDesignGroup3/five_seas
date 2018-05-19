package uk.ac.cam.cl.data;

/**
 * Encapsulates all units used for data measurements.
 *
 * @author Ben Cole
 */
public enum Unit {

  METERS("m"),
  FEET("ft"),
  KILOMETERS("km"),
  MILES("mi"),
  KILOMETERS_PER_HOUR("kph"),
  MILES_PER_HOUR("mph"),
  CELSIUS("°C"),
  FAHRENHEIT("°F");

  private String display;
  Unit(String display) {
    this.display = display;
  }

  @Override
  public String toString() {
    return display;
  }

  /**
   * Determines which unit type corresponds to the given string.
   *
   * @param string
   * @return
   */
  public static Unit fromString(String string) {
    for (Unit unit : Unit.values()) {
      if (unit.display.equals(string)) {
        return unit;
      }
    }
    return null;  // Should never be executed
  }

}
