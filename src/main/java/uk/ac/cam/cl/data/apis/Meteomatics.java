package uk.ac.cam.cl.data.apis;

import com.github.kevinsawicki.http.HttpRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataPoint;
import uk.ac.cam.cl.data.apis.API;

/**
 * Implementation of the Meteomatics API
 * @author Nathan Corbyn
 */
public class Meteomatics implements API {
    private String apiURL, requestBody, user, password;

    @Override
    public void initFromConfig(Config config) throws ConfigurationException {
        apiURL = (String) config.get("api_url");

        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("ZP");
        requestBuilder.append((String) config.get("api_period"));
        requestBuilder.append(":PT");
        requestBuilder.append((String) config.get("api_interval"));
        requestBuilder.append("/");

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
    public List<DataPoint> getProcessedData(JSONObject data) {
        //TODO process data
        return null;
    }

    @Override
    public JSONObject getData(double longitude, double latitude) 
            throws APIRequestException {
        try {
            DateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            String time = format.format(new Date()) + "T00:00:00Z/"; 
            HttpRequest request = HttpRequest.get("https://" +
                    user + ":" +
                    password + "@" +
                    apiURL + 
                    time + 
                    requestBody +
                    latitude + "," + longitude +
                    "/json?model=mix", true); 
            
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

