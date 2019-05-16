package weather;

import java.util.List;

public interface LocationWeather {
    // Interface that all other things will interact with to get information for a day
    // Immutable, refreshed by regenerating

    // Methods
    public List<Integer> giveDays(); // Returns days of the week available in YYYYMMDD format
    public List<Integer> giveHours(int date); // Returns hours available for given day in HH format
    public WeatherData giveData(int date, int hour); // Returns data for a given hour in weather data format
}
