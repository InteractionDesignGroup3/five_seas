package uk.ac.cam.cl.data.apis;

import com.github.kevinsawicki.http.HttpRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.ac.cam.cl.data.Config;
import uk.ac.cam.cl.data.ConfigurationException;
import uk.ac.cam.cl.data.DataManager;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HereMaps {
    private static HereMaps instance = null;
    private static String apiURL, appId, appCode;
    private static DataManager dataManager = DataManager.getInstance();

    private HereMaps() {
        Config conf;
        try {
            conf = new Config(Paths.get("here.json"));
            apiURL  = (String) conf.get("api_url");
            appId   = (String) conf.get("app_id");
            appCode = (String) conf.get("app_code");

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static HereMaps getInstance() {
        if (instance == null) {
            instance = new HereMaps();
        }

        return instance;
    }

    public List<String> getPlaces(String query) {
        List<String> ret = new ArrayList<>();

        HttpRequest request = HttpRequest.get(apiURL, false,
                "at", dataManager.getLatitude() + "," + dataManager.getLongitude(), // location
                "q", query,
                "app_id", appId,
                "app_code", appCode,
                "tf", "plain"
            ).accept("application/json");

        if (request.ok()) {
            System.out.println(request);
            String response = request.body();
            System.out.println(response);
            try {
                JSONParser parser = new JSONParser();
                JSONObject results = (JSONObject) parser.parse(response);
                JSONArray places = (JSONArray) results.get("results");

                for (int i=0; i<places.size(); i++) {
                    ret.add((String) ((JSONObject) places.get(i)).get("title"));
                }

            } catch (ParseException e){
                e.printStackTrace();
            }
        }

        return ret;
    }
}
