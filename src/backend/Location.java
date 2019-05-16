package backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class Location {

    private static String GEOCODE_TOKEN = "893f62730e9a83";

    private static String GEOCODE_BASE_URL = "https://eu1.locationiq.com/v1/search.php?key=%s&q=%s&format=json";
    private static String PLACES_BASE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/suggest" +
            "?text=%s" +
            "&f=json";

    private String GeocodeSearchUrl;

    private double lat;
    private double lon;
    private String placeId;

    public Location (String input) {

        this.GeocodeSearchUrl = String.format(GEOCODE_BASE_URL, GEOCODE_TOKEN, input);

        try {

            URL url = new URL(GeocodeSearchUrl);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray rootArr = root.getAsJsonArray(); // convert json element to json array as the output gives an array of json objects
            JsonObject rootObj = rootArr.get(0).getAsJsonObject(); // get the first json from the array and convert that to json object

            this.lat = rootObj.get("lat").getAsDouble();
            this.lon = rootObj.get("lon").getAsDouble();
            this.placeId = rootObj.get("place_id").getAsString();

        }

        catch (Exception e) {
            this.lat = 0;
            this.lon = 0;
            this.placeId = "nowhere";

        }

    } // end constructor

    Location () { // user default location

        this.lat = 52.2107375;
        this.lon = 0.09179849999999999;
        this.placeId = "333245852306";

    }

    public static String[] getLocation(String input, boolean nearMe) throws java.io.IOException {

        String placeSearchUrl = String.format(PLACES_BASE_URL, input);
        if (nearMe) {
            placeSearchUrl += String.format("&location=%s", "52.2107375,0.09179849999999999"); // Cambridge WGB
            // TODO : replace with current user's location
        }
        URL url = new URL(placeSearchUrl);
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootObj = root.getAsJsonObject(); // convert json element to json object as the output gives one big json object
        JsonArray suggestions = rootObj.get("suggestions").getAsJsonArray(); // get the JsonArray of all the suggested locations

        String[] suggestionList = new String[suggestions.size()];

        for (int i = 0; i < suggestions.size(); i++) {
            suggestionList[i] = suggestions.get(i).getAsJsonObject().get("test").getAsString();
            // store the suggestions in an array in the form of strings
        } // end for

        return suggestionList.clone();

    }


    public double getLat () {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPlaceId () {
        return placeId;
    }
}
