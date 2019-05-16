package backend;

import weather.LocationWeatherOWM;
import weather.WeatherData;

import java.io.IOException;

public class APIDemo {
    public static void main(String[] args) throws IOException {
        Location location = new Location("Cambridge");
        LocationWeatherOWM weather = new LocationWeatherOWM(location.getLon(), location.getLat());

        System.out.println("Available days:\t" + expandArray(weather.giveDays()));
        System.out.println("Hours for those days:");
        for (int day : weather.giveDays()) {
            System.out.println(expandArray(weather.giveHours(day)));
        }

        WeatherData data = weather.giveData(20190517, 0);
        
        System.out.println("\nTemperature at midday tomorrow:");
        System.out.println(data.getTemp());
    }

    private static String expandArray(int[] a) {
        String str = "";
        for (int i : a) {
            str += i + "\t";
        }

        return str;
    }
}
