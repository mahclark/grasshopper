package fx;

import backend.Location;
import weather.LocationWeatherOWM;
import weather.NoInternetConnection;
import weather.WeatherData;

import java.io.IOException;
import java.util.*;

public class StrategyGenerator {

    private LocationWeatherOWM weather;
    private int date;
    private String output;

    private List<Integer> relevantWeatherHours;

    /*Strategies to be determined:
     *
     *  -   Type of bowler      :   tall fast, spinners, swing
     *  -   Batting style       :   play along the ground, aeriel, harder
     *  -   Field placement     :   deep, aggressively
     *  -   Toss choice         :   bat first, bowl first
     *
     *Considered factors:
     *
     *  -   Temperature
     *  -   Humidity
     *  -   Wind speed/direction
     *  -   Precipitation
     *  -   Cloud coverage
     *  -   Sunset?
     *
     *Justifications?
     */

    public StrategyGenerator(LocationWeatherOWM weather, int date, int startHour, int overs) throws MultiDayGameException {
        this.weather = weather;
        this.date = date;

        int gamePeriod = (int) Math.round(0.5 + overs*3/20.0);
        relevantWeatherHours = new ArrayList<>();

        int start = startHour - startHour%3;
        int end = Math.max(startHour + gamePeriod - (startHour + gamePeriod)%3, start + 3);

        if (end == 24) {
            start = 18; end = 21;
        } else if (end > 24) {
            throw new MultiDayGameException();
        }

        for (int i = start; i <= end; i += 3) {
            relevantWeatherHours.add(i);
        }

        Map<Integer, Map<String, Map<String, Double>>> hourlyStrategies = new HashMap<>();
        List<Map<String, String>> hourlyBest = new ArrayList<>();

        for (int hour : relevantWeatherHours) {
            WeatherData data = weather.giveData(date, hour);
            hourlyStrategies.put(hour, staticStrategy(data));
//            System.out.println("\nStrategy for " + hour + " o'clock:");
//            System.out.println(bestStrategies(hourlyStrategies.get(hour)));
            hourlyBest.add(bestStrategies(hourlyStrategies.get(hour)));
        }

        List<Map<String, String>> firstHalf = new ArrayList<>();
        List<Map<String, String>> secondHalf = new ArrayList<>();

        int batFirstScore = 0;
        for (int i = 0; i < (hourlyBest.size() + 1)/2; i++) {
            if (hourlyBest.get(i).get("batOrBowl").equals("bat")) {
                batFirstScore++;
            } else {
                batFirstScore--;
            }
            firstHalf.add(hourlyBest.get(i));
        }
        for (int i = (hourlyBest.size() + 1)/2; i < hourlyBest.size(); i++) {
            if (hourlyBest.get(i).get("batOrBowl").equals("bat")) {
                batFirstScore--;
            } else {
                batFirstScore++;
            }
            secondHalf.add(hourlyBest.get(i));
        }

        System.out.println();

        String output1 = "For the first half, bring on " + chooseOne(firstHalf, "bowler") + " bowlers. When batting, " + chooseOne(firstHalf, "battingStyle") +
                ". Fielders should be placed " + chooseOne(firstHalf, "fieldPlacement") + ". It's a good time to " + chooseOne(firstHalf, "batOrBowl") + ".";

        String output2 = "For the second half, bring on " + chooseOne(secondHalf, "bowler") + " bowlers. When batting, " + chooseOne(secondHalf, "battingStyle") +
                ". Fielders should be placed " + chooseOne(secondHalf, "fieldPlacement") + ". It's a good time to " + chooseOne(secondHalf, "batOrBowl") + ".";

        String output3 = "If you win the toss, choose to " + (batFirstScore >= 0 ? "bat" : "bowl") + " first.";

//        System.out.println(output1);
//        System.out.println(output2);
//        System.out.println(output3);

        output = output1 + "\n\n" + output2 + "\n\n" + output3;

//        System.out.println("\nFirst half:");
//        List<String> types = new ArrayList<>(Arrays.asList("bowler", "battingStyle", "fieldPlacement", "batOrBowl"));
//        for (int i = 0; i < 4; i++) {
//            System.out.println(chooseOne(firstHalf, types.get(i)));
//        }
//
//
//        System.out.println("\nSecond half:");
//        for (int i = 0; i < 4; i++) {
//            System.out.println(chooseOne(secondHalf, types.get(i)));
//        }
    }

    public String getOutput() {
        return output;
    }

    public List<Integer> getRelevantWeatherHours() {
        return relevantWeatherHours;
    }

    private String chooseOne(List<Map<String, String>> hourlyStrategies, String type) {
        Map<String, Integer> count = new HashMap<>();

        for (Map<String, String> strategy : hourlyStrategies) {
            if (!count.containsKey(strategy.get(type))) {
                count.put(strategy.get(type), 0);
            }
            count.put(strategy.get(type), count.get(strategy.get(type)) + 1);
        }

        double maxValueInMap = Collections.max(count.values());
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Map<String, String> bestStrategies(Map<String, Map<String, Double>> strategy) {
        Map<String, String> best = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : strategy.entrySet()) {
            double maxValueInMap = Collections.max(entry.getValue().values());
            for (Map.Entry<String, Double> subEntry : entry.getValue().entrySet()) {
                if (subEntry.getValue() == maxValueInMap) {
                    best.put(entry.getKey(), subEntry.getKey());
                    break;
                }
            }
        }

        return best;
    }

    private Map<String, Map<String, Double>> staticStrategy(WeatherData data) {
        double temp = data.getTemp();               //degrees C
        double humidity = data.getHumidity();       //0-100
        double windSpeed = data.getWindSpeed();     //mps
        double cloud = data.getClouds();            //0-100

        Map<String, Map<String, Double>> strategyMap = makeStrategyMap(); //try to keep -10<value<10

        //High temp
        add(strategyMap, "bowler", "tall & fast", temp - 15);
        add(strategyMap, "battingStyle", "play along the ground", temp - 15);
        add(strategyMap, "fieldPlacement", "deep", temp - 15);
        add(strategyMap, "batOrBowl", "bat", temp - 15);
        //toss?

        //Low temp
        add(strategyMap, "bowler", "spin", 15 - temp);
        add(strategyMap, "battingStyle", "hit hard", 15 - temp);
        add(strategyMap, "batOrBowl", "bowl", 15 - temp);

        //High humidity
        add(strategyMap, "bowler", "swing", humidity/10 - 4);
        add(strategyMap, "batOrBowl", "bat", -Math.pow(humidity,2)/2000);

        //Rain
        //Lack thereof

        //High winds
        add(strategyMap, "bowler", "swing", windSpeed - 5);
        add(strategyMap, "bowler", "tall & fast", (windSpeed - 5)/2);
        add(strategyMap, "batOrBowl", "bowl", (windSpeed - 5)/2);

        //Clouds
        add(strategyMap, "bowler", "tall & fast", 5 - cloud/10);
        add(strategyMap, "battingStyle", "play aerially", (cloud - 50)/15); //replace with rain?
        add(strategyMap, "batOrBowl", "bat", 7 - cloud/10);

        return strategyMap;
    }

    private void add(Map<String, Map<String, Double>> map, String key1, String key2, double value) {
        map.get(key1).put(key2, map.get(key1).get(key2) + value);
    }

    private Map<String, Map<String, Double>> makeStrategyMap() {
        Map<String, Map<String, Double>> strategyMap = new HashMap<>();

        Map<String, Double> bowler = new HashMap<>();
        bowler.put("tall & fast", 0.0);
        bowler.put("spin", 0.0);
        bowler.put("swing", 0.0);

        Map<String, Double> battingStyle = new HashMap<>();
        battingStyle.put("play along the ground", 0.0);
        battingStyle.put("play aerially", 0.0);
        battingStyle.put("hit hard", 0.0);

        Map<String, Double> fieldPlacement = new HashMap<>();
        fieldPlacement.put("deep", 0.0);
        fieldPlacement.put("aggressively", 0.0);

        Map<String, Double> batOrBowl = new HashMap<>();
        batOrBowl.put("bat", 0.0);
        batOrBowl.put("bowl", 0.0);

        strategyMap.put("bowler", bowler);
        strategyMap.put("battingStyle", battingStyle);
        strategyMap.put("fieldPlacement", fieldPlacement);
        strategyMap.put("batOrBowl", batOrBowl);

        return strategyMap;
    }
}

class StrategyTester {
    public static void main(String[] args) throws NoInternetConnection, MultiDayGameException {
        StrategyGenerator generator = new StrategyGenerator(new LocationWeatherOWM(new Location("Cambridge")), 20190518, 12, 30);
        System.out.println(generator.getOutput());
    }
}

class MultiDayGameException extends Exception {
    public MultiDayGameException() {}
}