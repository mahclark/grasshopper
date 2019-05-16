package weather;

public interface WeatherData {
    // Interface that contains data for a given hour
    // Immutable

    // Methods
    public int getDateTime(); // Gives date and time in YYYYMMDD(HH) format

    // Get methods for all data
    public String getWeather();
    public String getDescription();
    public String getIconCode();
    public float getTemp();
    public float getTempMin();
    public float getTempMax();
    public float getPressure();
    public float getSeaLevel();
    public float getGroundLevel();
    public float getHumidity();
    public float getClouds();
    public float getWindSpeed();
    public float getWindDeg();
}
