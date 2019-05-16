package backend;

import com.google.gson.*;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;



public class Location {

    private static String LOCATION_TOKEN = "893f62730e9a83";
    private static String BASE_URL = "https://eu1.locationiq.com/v1/search.php?key=%s&q=%s&format=json";

    private String searchUrl;

    private double lat;
    private double lon;
    private String placeId;

    Location (String input) {

        this.searchUrl = String.format(BASE_URL, LOCATION_TOKEN, input);

        try {

            URL url = new URL(searchUrl);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray rootArr = root.getAsJsonArray();
            JsonObject rootObj = rootArr.get(0).getAsJsonObject();

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
