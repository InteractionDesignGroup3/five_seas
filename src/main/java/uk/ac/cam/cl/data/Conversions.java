package uk.ac.cam.cl.data;

/**
 * Class providing all used unit conversions
 *
 * @author Nathan Corbyn
 */
public class Conversions {
  // Unit conversion scale factors and offsets
  public static final double CELCIUS_OFFSET = 32.0,
      CELCIUS_SCALE = 1.8,
      KNOTS_SCALE = 0.539957,
      MILE_SCALE = 0.621371,
      MS_SCALE = 0.27777777,
      INCH_SCALE = 0.0393701,
      FEET_SCALE = 3.28084;

  public static double celciusToFarenheit(double celcius) {
    return CELCIUS_OFFSET + CELCIUS_SCALE * celcius;
  }

  public static double kmPHToKnots(double kmph) {
    return KNOTS_SCALE * kmph;
  }

  public static double kmPHToMPH(double kmph) {
    return MILE_SCALE * kmph;
  }

  public static double kmPHToMS(double kmph) {
    return MS_SCALE * kmph;
  }

  public static double mmToInches(double mm) {
    return INCH_SCALE * mm;
  }

  public static double mToFeet(double m) {
    return FEET_SCALE * m;
  }

  public static double kmToMiles(double km) {
    return MILE_SCALE * km;
  }
}
