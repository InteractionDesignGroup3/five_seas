package uk.ac.cam.cl.data;

/**
 * Class used by the data manager to represent data sequences
 * (also contains any unit conversions for such data)
 * @author Nathan Corbyn
 */
public class DataPoint implements Comparable<DataPoint> {
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
           tideHeight,                //Tide height in metres
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
     * @param tideHeight tide height in metres
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
            double tideHeight,
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
        this.tideHeight = tideHeight;
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

    public double getTemperatureCelcius() {
        return temperature;
    }

    public double getTemperatureFarenheit() {
        return Conversions.celciusToFarenheit(temperature);
    }

    public double getFeelsLikeTemperatureCelcius() {
        return feelsLikeTemperature;
    }

    public double getFeelsLikeTemperatureFarenheit() {
        return Conversions.celciusToFarenheit(feelsLikeTemperature);
    }

    public double getWindSpeedKmPH() {
        return windSpeedKmPH;
    }
    
    public double getWindSpeedKnots() {
        return Conversions.kmPHToKnots(windSpeedKmPH);
    }
    
    public double getWindSpeedMPH() {
        return Conversions.kmPHToMPH(windSpeedKmPH);
    }
    
    public double getWindSpeedMS() {
        return Conversions.kmPHToMS(windSpeedKmPH);
    }

    public double getGustSpeedKmPH() {
        return gustSpeedKmPH;
    }
    
    public double getGustSpeedKnots() {
        return Conversions.kmPHToKnots(gustSpeedKmPH);
    }
    
    public double getGustSpeedMPH() {
        return Conversions.kmPHToMPH(gustSpeedKmPH);
    }
    
    public double getGustSpeedMS() {
        return Conversions.kmPHToMS(gustSpeedKmPH);
    }

    public double getChanceOfRain() {
        return chanceOfRain;
    }

    public double getPrecipitationMM() {
        return precipitationMM;
    }
    
    public double getPrecipitationInches() {
        return Conversions.mmToInches(precipitationMM);
    }

    public double getSwellHeightM() {
        return swellHeight;
    }

    public double getSwellHeightFeet() {
        return Conversions.mToFeet(swellHeight);
    }

    public double getSwellPeriod() {
        return swellPeriod;
    }
    
    public double getTideHeightM() {
        return tideHeight;
    }

    public double getTideHeightFeet() {
        return Conversions.mToFeet(tideHeight);
    }

    public double getVisibilityKM() {
        return visibility;
    }

    public double getVisibilityMiles() {
        return Conversions.kmToMiles(visibility);
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
                tideHeight + "m, " + 
                visibility + "km, " +
                weatherCode + " }";
    }
}

