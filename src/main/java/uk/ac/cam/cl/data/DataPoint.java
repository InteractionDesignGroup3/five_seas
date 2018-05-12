package uk.ac.cam.cl.data;

/**
 * Class used by the data manager to represent data sequences
 * (also contains any unit conversions for such data)
 * @author Nathan Corbyn
 */
public class DataPoint implements Comparable<DataPoint> {
    //Unit conversion scale factors and offsets
    public static final double CELCIUS_OFFSET = 32.0,
           CELCIUS_SCALE = 1.8,
           KNOTS_SCALE = 0.539957,
           MILE_SCALE = 0.621371,
           MS_SCALE = 3.6,
           INCH_SCALE = 0.0393701,
           FEET_SCALE = 3.28084;

    //Actual data
    private final long time;
    private final double temperature, //Actual temperature in degrees C
           feelsLikeTemperature,      //Feels like temperature in degrees C
           gustSpeedKmPH,             //Gust speed in kilometres per hour
           windSpeedKmPH,             //Wind speed in kilometres per hour
           chanceOfRain,              //Chance of rain %
           precipitationMM,           //Precipitation in millimetres
           swellHeight,               //Swell height in metres
           swellPeriod,               //Swell period in seconds
           visibility;                //Visibility in kilometres
    private final int weatherCode;    //Used for weather overview

    /**
     * Instantiates a new data point with all of the necessary information
     * @param time time corresponding to data (unix)
     * @param temperature actual temperature in degrees C
     * @param feelsLikeTemperature feels like temperature in degrees C
     * @param gustSpeedKmPH gust speed in kilometres per hour
     * @param windSpeedKmPH wind speed in kilometres per hour
     * @param chanceOfRain chance of rain %
     * @param precipitationMM precipiration in millimetres
     * @param swellHeight swell height in metres
     * @param swellPeriod swell period in seconds
     * @param visibility visibility in kilometres
     * @param weatherCode a code used to represent the weather overview
     */
    public DataPoint(long time, 
            double temperature, 
            double feelsLikeTemperature, 
            double gustSpeedKmPH, 
            double windSpeedKmPH, 
            double chanceOfRain, 
            double precipitationMM, 
            double swellHeight, 
            double swellPeriod, 
            double visibility, 
            int weatherCode) {
        this.time = time;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.windSpeedKmPH = windSpeedKmPH;
        this.gustSpeedKmPH = gustSpeedKmPH;
        this.chanceOfRain = chanceOfRain;
        this.precipitationMM = precipitationMM;
        this.swellHeight = swellHeight;
        this.swellPeriod = swellPeriod;
        this.visibility = visibility;
        this.weatherCode = weatherCode;
    }

    /**
     * Orders data points by their time stamp
     * @param other the data point to compare to
     * @return an integer representing the computed ordering
     */
    @Override
    public int compareTo(DataPoint other) {
        return (new Long(time)).compareTo(new Long(other.time));
    }

    //These are all basic unit conversion methods implemented using
    //the constants at the top of the class
    private static double celciusToFarenheit(double celcius) {
        return CELCIUS_OFFSET + CELCIUS_SCALE * celcius; 
    }

    private static double kmPHToKnots(double kmph) {
        return KNOTS_SCALE * kmph;
    }

    private static double kmPHToMPH(double kmph) {
        return MILE_SCALE * kmph;
    }

    private static double kmPHToMS(double kmph) {
        return MS_SCALE * kmph;
    }

    private static double mmToInches(double mm) {
        return INCH_SCALE * mm;
    }

    private static double mToFeet(double m) {
        return FEET_SCALE * m;
    }

    private static double kmToMiles(double km) {
        return MILE_SCALE * km;
    }

    public double getTemperatureCelcius() {
        return temperature;
    }

    public double getTemperatureFarenheit() {
        return celciusToFarenheit(temperature);
    }

    public double getFeelsLikeTemperatureCelcius() {
        return feelsLikeTemperature;
    }

    public double getFeelsLikeTemperatureFarenheit() {
        return celciusToFarenheit(feelsLikeTemperature);
    }

    public double getWindSpeedKmPH() {
        return windSpeedKmPH;
    }
    
    public double getWindSpeedKnots() {
        return kmPHToKnots(windSpeedKmPH);
    }
    
    public double getWindSpeedMPH() {
        return kmPHToMPH(windSpeedKmPH);
    }
    
    public double getWindSpeedMS() {
        return kmPHToMS(windSpeedKmPH);
    }

    public double getGustSpeedKmPH() {
        return gustSpeedKmPH;
    }
    
    public double getGustSpeedKnots() {
        return kmPHToKnots(gustSpeedKmPH);
    }
    
    public double getGustSpeedMPH() {
        return kmPHToMPH(gustSpeedKmPH);
    }
    
    public double getGustSpeedMS() {
        return kmPHToMS(gustSpeedKmPH);
    }

    public double getChanceOfRain() {
        return chanceOfRain;
    }

    public double getPrecipitationMM() {
        return precipitationMM;
    }
    
    public double getPrecipitationInches() {
        return mmToInches(precipitationMM);
    }

    public double getSwellHeightM() {
        return swellHeight;
    }

    public double getSwellHeightFeet() {
        return mToFeet(swellHeight);
    }

    public double getSwellPeriod() {
        return swellPeriod;
    }

    public double getVisibilityKM() {
        return visibility;
    }

    public double getVisibilityMiles() {
        return kmToMiles(visibility);
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    /**
     * Formatted string containing all values in their default units
     * @return formatted data point string
     */
    @Override
    public String toString() {
        return "{ " + time + ", " + 
                temperature + "degC, " + 
                feelsLikeTemperature + "degC, " +
                gustSpeedKmPH + "kmph, " +
                windSpeedKmPH + "kmph, " +
                chanceOfRain + "%, " +
                precipitationMM + "mm, " +
                swellHeight + "m, " +
                swellPeriod + "m, " +
                visibility + "km, " +
                weatherCode + " }";
    }
}

