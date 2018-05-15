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

/**
 * Implementation of the World Weather Online API
 * @author Nathan Corbyn
 */
public class WorldWeatherOnline implements API<DataSequence> {
    private String api, token, marine, local;

    @Override
    public void initFromConfig(Config config) throws ConfigurationException {
        api = (String) config.get("api_url");
        marine = (String) config.get("marine_api");
        local = (String) config.get("local_api");
        token = (String) config.get("api_token");
    }

    @Override
    public List<DataSequence> getProcessedData(JSONObject data) 
            throws APIRequestException {
        List<DataSequence> sequences = new ArrayList<>();

        JSONObject dump = (JSONObject) data.get("dump");
        JSONObject dumpData = (JSONObject) dump.get("data");
        JSONArray weather = (JSONArray) dumpData.get("weather");
        for (int i = 0; i < weather.size(); i++) {
            List<DataPoint> points = new ArrayList<>();
            JSONObject current = (JSONObject) weather.get(i);
            JSONArray hourly = (JSONArray) current.get("hourly");

            try {
                DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                Date date = format.parse((String) current.get("date"));

                for (int j = 0; j < hourly.size(); j++) {
                    JSONObject hour = (JSONObject) hourly.get(j);
                    long time = date.getTime() + 3600000 * j;
                    DataPoint point = new DataPoint(time,
                            Double.parseDouble((String) hour.get("tempC")),
                            Double.parseDouble((String) hour.get("FeelsLikeC")),
                            Double.parseDouble((String) hour.get("windspeedKmph")),
                            Double.parseDouble((String) hour.get("WindGustKmph")),
                            0.0, //TODO get real chance of rain
                            Double.parseDouble((String) hour.get("precipMM")),
                            Double.parseDouble((String) hour.get("swellHeight_m")),
                            Double.parseDouble((String) hour.get("swellPeriod_secs")),
                            0.0, //TODO get real tide height
                            Double.parseDouble((String) hour.get("visibility")),
                            Integer.parseInt((String) hour.get("weatherCode")));
                    points.add(point);
                }

                sequences.add(new DataSequence(date.getTime(), 
                            0.0, //TODO actual max temperature
                            0.0, //TODO actual min temperature
                            points));
            } catch (java.text.ParseException e) {
                throw new APIRequestException(); 
            }
        }
            
        return sequences;
    }

    @Override
    public JSONObject getData(double longitude, double latitude) 
            throws APIRequestException {
        try {
            HttpRequest request = HttpRequest.get(api + marine, true,
                    'q', latitude + "," + longitude, //Set query location
                    "date", "today",  //From today
                    "fx", "yes",      //Specify whether to include weather forecast
                    "format", "json", //Specify format (default is XML)
                    "key", token,     //Pass API token
                    "tp", 1,          //Sets the time period for requests (hours)
                    "includelocation", "yes"); //Show location
            System.out.println(request.toString());

            if (request.ok()) {
                String response = request.body();
                try {
                    JSONParser parser = new JSONParser();
                    return (JSONObject) parser.parse(response);
                } catch (ParseException e) { 
                    e.printStackTrace(); 
                    throw new APIRequestException(); 
                }
            } else throw new APIRequestException();
        } catch (HttpRequest.HttpRequestException e) { 
            e.printStackTrace(); 
            throw new APIRequestException(); 
        }
    }
    
    @Override
    public JSONObject getData(double longitude, double latitude, String target) 
            throws APIRequestException {
        return getData(longitude, latitude);
    }
}

