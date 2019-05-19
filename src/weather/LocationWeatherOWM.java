package weather;

// Imports for GSON
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Imports for location
import backend.Location;

// Imports for URL
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class LocationWeatherOWM implements LocationWeather {
    // States
    private String apiLocation = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=493b5a3ed493b5c87ffbcd84555d811f"; // URL for JSON
    private Map<Integer, WeatherData> data;

    // Class constructor
    private LocationWeatherOWM(double latitude, double longitude) throws NoInternetConnection {
        try {
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
            JsonArray list = (JsonArray) rootobj.get("list"); // List of data

            // Item to use when iterating through weather
            JsonObject current;
            JsonObject main;
            JsonObject weather;
            JsonObject clouds;
            JsonObject wind;
            WeatherData weatherData;
            String dateTimeText;
            Integer dateTimeNumber;

            // Creates map
            data = new HashMap<>();

            // Begins generating items
            for (JsonElement element : list) {
                // Converts + finds date time
                current = (JsonObject) element;
                dateTimeText = current.get("dt_txt").toString();
                dateTimeNumber = Integer.parseInt(dateTimeText.substring(1, 5).concat(dateTimeText.substring(6, 8)).concat(dateTimeText.substring(9, 11)).concat(dateTimeText.substring(12, 14)));

                // Gets each object
                main = current.getAsJsonObject("main");
                weather = current.getAsJsonArray("weather").get(0).getAsJsonObject();
                clouds = current.getAsJsonObject("clouds");
                wind = current.getAsJsonObject("wind");

                // Creates weather data
                weatherData = new WeatherDataStandard(dateTimeNumber,
                        weather.get("main").getAsString(),
                        weather.get("description").getAsString(),
                        weather.get("icon").getAsString(),
                        Math.round(main.get("temp").getAsFloat() - 273.15),
                        Math.round(main.get("temp_min").getAsFloat() - 273.15),
                        Math.round(main.get("temp_max").getAsFloat() - 273.15),
                        main.get("pressure").getAsFloat(),
                        main.get("sea_level").getAsFloat(),
                        main.get("grnd_level").getAsFloat(),
                        main.get("humidity").getAsFloat(),
                        clouds.get("all").getAsFloat(),
                        wind.get("speed").getAsFloat(),
                        wind.get("deg").getAsFloat());

                // Adds to map
                data.put(dateTimeNumber, weatherData);
            }
        } catch (IOException e) {
            throw new NoInternetConnection();
        }
    }

    //  Class constructor for location
    public LocationWeatherOWM(Location location) throws NoInternetConnection {
        this(location.getLat(), location.getLon());
    }

    @Override
    public List<Integer> giveDays() {
        // Gets set of values
        Set<Integer> datetimes = data.keySet();
        // New set of dates
        Set<Integer> dates = new HashSet<>();
        // Adds all dates
        for (Integer datetime : datetimes){
            dates.add(datetime / 100);
        }
        // Returns in sorted array
        List<Integer> list = new ArrayList<>(dates);
        Collections.sort(list);
        return list;
    }

    @Override
    public List<Integer> giveHours(int date) {
        // Gets set of values
        Set<Integer> datetimes = data.keySet();
        // New set of dates
        Set<Integer> times = new HashSet<>();
        // Adds all times for day
        for (Integer datetime : datetimes){
            if ((datetime / 100) == date){
                times.add(datetime % 100);
            }
        }
        // Returns in sorted array
        List<Integer> list = new ArrayList<>(times);
        Collections.sort(list);
        return list;
    }

    @Override
    public WeatherData giveData(int date, int hour) {
        // Converts to yyyymmddhh and gets WeatherData
        // Returns null if no value
        return data.get(date*100 + hour);
    }

    // Example for hourly
    public static void main(String[] args) throws NoInternetConnection {
        LocationWeather owm = new LocationWeatherOWM(52.211109, 0.091517);
        System.out.println("Result of 'owm.giveData(20190517, 12).getTemp()':");
        System.out.println(owm.giveData(20190517, 12).getTemp());
    }
}