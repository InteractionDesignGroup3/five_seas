package uk.ac.cam.cl.data;

/**
 * Encapsulates all units used for data measurements.
 *
 * @author Ben Cole
 */
public enum Unit {
  MILIMETERS("mm"),
  INCHES("in"),
  METERS("m"),
  FEET("ft"),
  KILOMETERS("Km"),
  MILES("mi"),
  KILOMETERS_PER_HOUR("Km/h"),
  MILES_PER_HOUR("mph"),
  CELSIUS("°C"),
  FAHRENHEIT("°F"),
  NONE("");

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
    return NONE;  // Should never be executed
  }
}
