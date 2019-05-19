package weather;

import backend.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherDataCurrent{

    // States
    private String apiLocation = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid=493b5a3ed493b5c87ffbcd84555d811f"; // URL for JSON

    private int dateTime;
    private String weather;
    private String description;
    private String iconCode;
    private float temp;
    private float tempMin;
    private float tempMax;
    private float pressure;
    private float humidity;
    private float clouds;
    private float windSpeed;
    private float windDeg;

    // Constructor
    private WeatherDataCurrent(double latitude, double longitude) throws IOException {
        // Adapt URL with long+lat
        apiLocation = apiLocation.replace("{lat}", Double.toString(latitude)).replace("{lon}", Double.toString(longitude));

        // Connect to the URL using URL
        URL url = new URL(apiLocation);
        URLConnection request = url.openConnection();
        request.connect();

        // Convert to a JSON object
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); // Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); // Get object downloaded

        // Gets weather
        JsonArray weathera = (JsonArray) rootobj.get("weather"); // List of dat
        JsonObject weather =(JsonObject) weathera.get(0);

        // Gets main
        JsonObject main = (JsonObject) rootobj.get("main");

        // Gets wind
        JsonObject wind = (JsonObject) rootobj.get("wind");

        // Gets clouds
        JsonObject clouds = (JsonObject) rootobj.get("clouds");
    }

    //  Class constructor for location
    public WeatherDataCurrent(Location location) throws IOException {
        this(location.getLat(), location.getLon());
    }

    // Returns values
    public int getDateTime(){
        return dateTime;
    }

    public String getWeather() {
        return weather;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public float getTemp() {
        return temp;
    }

    public float getTempMin() {
        return tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getClouds() {
        return clouds;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getWindDeg() {
        return windDeg;
    }

    // Main
    public static void main(String[] args) throws IOException {
        WeatherDataCurrent wdc = new WeatherDataCurrent(52.211109, 0.091517);
    }
}