package uk.ac.cam.cl.data.apis;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataManager;
import uk.ac.cam.cl.data.Location;

/**
 * Implementation of the Here Maps API
 * @author Nathan Corbyn, Max Campman
 */
public class HereMaps implements API<Location> {
    private String apiURL, appId, appCode;

    public void initFromConfig(Config config) throws ConfigurationException {
        apiURL = (String) config.get("api_url");
        appId = (String) config.get("app_id");
        appCode = (String) config.get("app_code");
    }

    public List<Location> getProcessedData(JSONObject data)
            throws APIRequestException {
        List<Location> results = new ArrayList<>();
        JSONObject dump = (JSONObject) data.get("dump");
        JSONArray places = (JSONArray) dump.get("results");
        for (int i = 0; i < places.size(); i++) {
            JSONObject place = (JSONObject) places.get(i);
            JSONArray position = (JSONArray) place.get("position");
            
            double longitude = 0.0, latitude = 0.0;
            try {
                latitude = (Double) position.get(0);
                longitude = (Double) position.get(1);
            } catch (NullPointerException e) {
                latitude = (Double) dump.get("latitude");
                longitude = (Double) dump.get("longitude");
            }

            String title = (String) place.get("title");
            results.add(new Location(title, longitude, latitude));
        }

        return results;
    }

    public JSONObject getData(double longitude, double latitude) {
        return null;  
    }

    public JSONObject getData(double longitude, double latitude, String target)
            throws APIRequestException {
        try { 
            HttpRequest request = HttpRequest.get(apiURL, false,
                "at", latitude + "," + longitude, 
                "q", target,
                "app_id", appId,
                "app_code", appCode,
                "tf", "plain").accept("application/json");

            if (request.ok()) {
                System.out.println(request);
                String response = request.body();
                System.out.println(response);
                try {
                    JSONParser parser = new JSONParser();
                    return (JSONObject) parser.parse(response);
                } catch (ParseException e){
                    e.printStackTrace();
                    throw new APIRequestException();
                }
            } else throw new APIRequestException();
        } catch (HttpRequest.HttpRequestException e) {
            e.printStackTrace();
            throw new APIRequestException();
        }
    }
}
