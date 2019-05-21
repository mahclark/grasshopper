package backend;

import fx.Main;
import fx.UserEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventFunctions {

    // Returns events in even list (accounts for errors)
    public static List<UserEvent> getEvents(){
        // Tries to get event file, returns empty if none found
        File file = new File(Main.pathToEventFile);
        if (!file.exists()){
            return new ArrayList<>();
        }

        // Tries to get file, if error just goes to empty
        try {
            // Uses buffered reader to process line by line
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Gets current date to remove old values
            Date dateC = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            int currentDate = Integer.parseInt(formatter.format(dateC));

            // Variables for iteration
            String line = "initial";
            String[] values;
            int index;
            List<UserEvent> events = new ArrayList<>();


            String name;
            String locationInput;
            String locationID;
            double latitude;
            double longitude;
            int date;
            int startHour;
            int overs;

            // Iterates through lines
            while (line != null) {
                values = new String[8];
                index = 0;
                // Processes data, if error occurs will iterate until empty string reached
                while (((line = bufferedReader.readLine()) != null) && ((!line.equals("")))) {
                    // Only adds if index less than five
                    if (index < 8) {
                        values[index] = line;
                        System.out.println(line);
                    }
                    index++;
                }
                // Only adds if correct amounts
                if (index == 8) {
                    name = values[0];
                    locationInput = values[1];
                    locationID = values[2];
                    try {
                        latitude = Double.parseDouble(values[3]);
                        longitude = Double.parseDouble(values[4]);
                        date = Integer.parseInt(values[5]);
                        startHour = Integer.parseInt(values[6]);
                        overs = Integer.parseInt(values[7]);

                        if (date >= currentDate) {
                            events.add(new UserEvent(name, new Location(locationInput, locationID, latitude, longitude), date, startHour, overs));
                        }
                    }
                    catch (NumberFormatException e) {
                    }
                }
            }
            return events;
        }

        // Returns empty if not readable
        catch (IOException e){
            return new ArrayList<>();
        }
    }

    // Methods to save current events
    public static void saveEvents(List<UserEvent> events){
        // Creates writer for file
        try {
            PrintWriter writer = new PrintWriter(Main.pathToEventFile, "UTF-8");
            for (UserEvent event: events){
                //Prints required info then newline
                writer.println(event.getName());
                writer.println(event.getLocation().getInput());
                writer.println(event.getLocation().getPlaceId());
                writer.println(event.getLocation().getLat());
                writer.println(event.getLocation().getLon());
                writer.println(event.getDate());
                writer.println(event.getStartHour());
                writer.println(event.getOvers());
                writer.println("");
            }
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }

}
