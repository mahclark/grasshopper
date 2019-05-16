package weather;

public class WeatherDataStandard implements WeatherData {

    // States
    private int dateTime;
    private String weather;
    private String description;
    private String iconCode;
    private float temp;
    private float tempMin;
    private float tempMax;
    private float pressure;
    private float seaLevel;
    private float groundLevel;
    private float humidity;
    private float clouds;
    private float windSpeed;
    private float windDeg;

    // Standard Constructor
    WeatherDataStandard( Integer dateTime,
            String weather,
            String description,
            String iconCode,
            float temp,
            float tempMin,
            float tempMax,
            float pressure,
            float seaLevel,
            float groundLevel,
            float humidity,
            float clouds,
            float windSpeed,
            float windDeg){
        // Assigns
        this.dateTime = dateTime;
        this.weather = weather;
        this.description = description;
        this.iconCode = iconCode;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;

    }

    // Returns values
    @Override
    public int getDateTime(){
        return dateTime;
    }

    @Override
    public String getWeather() {
        return weather;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIconCode() {
        return iconCode;
    }

    @Override
    public float getTemp() {
        return temp;
    }

    @Override
    public float getTempMin() {
        return tempMin;
    }

    @Override
    public float getTempMax() {
        return tempMax;
    }

    @Override
    public float getPressure() {
        return pressure;
    }

    @Override
    public float getSeaLevel() {
        return seaLevel;
    }

    @Override
    public float getGroundLevel() {
        return groundLevel;
    }

    @Override
    public float getHumidity() {
        return humidity;
    }

    @Override
    public float getClouds() {
        return clouds;
    }

    @Override
    public float getWindSpeed() {
        return windSpeed;
    }

    @Override
    public float getWindDeg() {
        return windDeg;
    }
}
