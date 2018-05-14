package uk.ac.cam.cl.data.apis;

import com.github.kevinsawicki.http.HttpRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.DataSequence;
import uk.ac.cam.cl.data.apis.API;

/**
 * Implementation of the Meteomatics API
 * @author Nathan Corbyn
 */
public class Meteomatics implements API {
    private String apiURL, requestBody, user, password;
    private long period, interval;

    private JSONArray getSequence(JSONArray dumpData, int i) {
        JSONObject parameter = (JSONObject) dumpData.get(i);
        JSONArray coordinates = (JSONArray) parameter.get("coordinates");
        JSONObject target = (JSONObject) coordinates.get(0);
        return (JSONArray) target.get("dates"); 
    }

    private double getValue(JSONArray sequence, int j) {
        JSONObject point = (JSONObject) sequence.get(j);
        try {
            return (Double) point.get("value");
        } catch (ClassCastException e) {
            return (double) ((Long) point.get("value"));
        }
    }

    @Override
    public void initFromConfig(Config config) throws ConfigurationException {
        interval = (Long) config.get("api_interval"); 
        period = (Long) config.get("api_period"); 
        apiURL = (String) config.get("api_url");

        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("P");
        requestBuilder.append(interval);
        requestBuilder.append("D:PT");
        requestBuilder.append(period);
        requestBuilder.append("M/");

        JSONArray parameters = (JSONArray) config.get("api_parameters");
        for (int i = 0; i < parameters.size(); i++) {
            requestBuilder.append((String) parameters.get(i));
            if (i < parameters.size() - 1)
                requestBuilder.append(",");
            else requestBuilder.append("/");
        }

        requestBody = requestBuilder.toString();
        user = (String) config.get("api_user");
        password = (String) config.get("api_password");
    }

    @Override
    public List<DataSequence> getProcessedData(JSONObject data) 
            throws APIRequestException {
        List<DataSequence> sequences = new ArrayList<>();
        
        JSONObject dump = (JSONObject) data.get("dump");
        JSONArray dumpData = (JSONArray) dump.get("data");

        JSONArray temperature = getSequence(dumpData, 0);
        JSONArray windSpeed = getSequence(dumpData, 1);
        JSONArray windDirection = getSequence(dumpData, 2);
        JSONArray windGusts = getSequence(dumpData, 3);
        JSONArray precipitation = getSequence(dumpData, 4); 
        JSONArray chanceOfRain = getSequence(dumpData, 5);
        JSONArray swellHeight = getSequence(dumpData, 6);
        JSONArray swellPeriod = getSequence(dumpData, 7);
        JSONArray tideHeight = getSequence(dumpData, 8);
        JSONArray visibility = getSequence(dumpData, 9);
        JSONArray code = getSequence(dumpData, 10);

        DateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");

        try {
            for (int i = 0; i < interval; i++) {
                List<DataPoint> points = new ArrayList<>();
               
                String date = (String) ((JSONObject) code.get(0)).get("date");
                long time = format.parse(date).getTime();
                        
                for (int j = 0; j < temperature.size(); j++) {
                    long pointTime = time + (i * 24 * 60 + j * period) * 60;
                    points.add(new DataPoint(pointTime,
                            getValue(temperature, j),
                            getValue(windSpeed, j), 
                            getValue(windDirection, j), 
                            getValue(windGusts, j), 
                            getValue(precipitation, j), 
                            getValue(chanceOfRain, j), 
                            getValue(swellHeight, j), 
                            getValue(swellPeriod, j), 
                            getValue(tideHeight, j),
                            getValue(visibility, j),
                            (Long) ((JSONObject) code.get(j)).get("value"))); 
                }

                sequences.add(new DataSequence(time, 0.0, 0.0, points));
            }
        } catch (java.text.ParseException e) { throw new APIRequestException(); }

        return sequences;
    }

    @Override
    public JSONObject getData(double longitude, double latitude) 
            throws APIRequestException {
        try {
            DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            String time = format.format(new Date()) + "T00:00:00Z"; 
            HttpRequest request = HttpRequest.get("https://" +
                    apiURL + 
                    time + 
                    requestBody +
                    latitude + "," + longitude +
                    "/json?model=mix", true).basic(user, password); 
            
            if (request.ok()) { 
                String response = request.body();
                try {
                    JSONParser parser = new JSONParser();
                    return (JSONObject) parser.parse(response);
                } catch (ParseException e) { 
                    //Handled by the connector
                    e.printStackTrace(); 
                    throw new APIRequestException();  
                }
            } else throw new APIRequestException();  
        } catch (HttpRequest.HttpRequestException e) {
            //Handled by the connector
            e.printStackTrace(); 
            throw new APIRequestException();  
        }
    }
}

