package backend;

import fx.Event;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventFunctions {

    // Returns events in even list (accounts for errors)
    public static List<Event> getEvents(){
        // Tries to get event file, returns empty if none found
        String filepath = "resources/event.txt";
        File file = new File(filepath);
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
            List<Event> events = new ArrayList<>();


            String name;
            String locationName;
            int date;
            int startHour;
            int overs;

            // Iterates through lines
            while (line != null) {
                values = new String[5];
                index = 0;
                // Processes data, if error occurs will iterate until empty string reached
                while (((line = bufferedReader.readLine()) != null) && ((!line.equals("")))) {
                    // Only adds if index less than five
                    if (index < 5) {
                        values[index] = line;
                    }
                    index++;
                }
                // Only adds if correct amounts
                if (index == 5) {
                    name = values[0];
                    locationName = values[1];
                    try {
                        date = Integer.parseInt(values[2]);
                        startHour = Integer.parseInt(values[3]);
                        overs = Integer.parseInt(values[4]);

                        if (date >= currentDate) {
                            events.add(new Event(name, new Location(locationName), date, startHour, overs));
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
    public static void saveEvents(List<Event> events){
        // Creates writeer for file
        String filepath = "resources/event.txt";
        try {
            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            for (Event event: events){
                //Prints required info then newline
                writer.println(event.getName());
                writer.println(event.getLocationName());
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
