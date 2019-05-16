package backend;

import weather.LocationWeatherOWM;
import weather.WeatherData;

import java.io.IOException;

public class APIDemo {
    public static void main(String[] args) throws IOException {
        Location location = new Location("Cambridge");
        LocationWeatherOWM weather = new LocationWeatherOWM(location.getLat(), location.getLon());

        System.out.println("\nAvailable days:\t" + expandArray(weather.giveDays()));
        System.out.println("Hours for those days:");
        for (int day : weather.giveDays()) {
            System.out.println(day + ":\t" + expandArray(weather.giveHours(day)));
        }

        WeatherData data = weather.giveData(weather.giveDays()[1], 12);

        System.out.println("\nMidday Tomorrow");
        System.out.println("---------------");
        System.out.println("Title:\t\t\t" + data.getWeather());
        System.out.println("Description:\t" + data.getDescription());
        System.out.println("Temperature:\t" + Math.round(data.getTemp() - 273.15));
        System.out.println("Humidity:\t\t" + data.getHumidity());
        System.out.println("Cloud cover:\t" + data.getClouds());
    }

    private static String expandArray(int[] a) {
        String str = "";
        for (int i : a) {
            str += i + "\t";
        }

        return str;
    }
}
