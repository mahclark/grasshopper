package backend;

import fx.Main;

import java.io.*;
import java.util.ArrayList;

public class SettingsFunctions {
    public static boolean getNotificationStatus(){

        // Tries to get file with setting
        File file = new File(Main.pathToNotificationsFile);
        if (!file.exists()){
            return true;
        }

        // If file exists, reads, and if set for false, changes to false, otherwise defaults to true
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            if (line.equals("false")){
                return false;
            }
            else{
                return true;
            }
        }
        catch (IOException e){
            return true;
        }
    }

    public static boolean getTemperatureStatus(){

        // Tries to get file with setting
        File file = new File(Main.pathToTemperatureFile);
        if (!file.exists()){
            return false;
        }

        // If file exists, reads, and if set for true, changes to true, otherwise defaults to false
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            if (line.equals("true")){
                return true;
            }
            else{
                return false;
            }
        }
        catch (IOException e){
            return false;
        }
    }

    public static Location getLocation(){

        // Tries to get file with setting
        File file = new File(Main.pathToLocationFile);
        if (!file.exists()){
            return new Location("Cambridge", "333245852306", 52.2107375, 0.09179849999999999);
        }

        // If file exists, reads, and if set for false, changes to false, otherwise defaults to true
        try {
            String[] strings = new String[4];
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            for (int i = 0; i<4; i++){
                strings[i] = bufferedReader.readLine();
                if (strings[i]==null || strings[i].equals("")){
                    throw new IOException();
                }
            }
            return new Location(strings[0], strings[1], Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
        }
        catch (IOException e){
            return new Location("Cambridge", "333245852306", 52.2107375, 0.09179849999999999);
        }
    }

    public static void setNotificationStatus(Boolean status){
        // Creates writer for file
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(Main.pathToLocationFile, "UTF-8");
            writer.println(status);
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }

    public static void setTemperatureStatus(Boolean status){
        // Creates writer for file
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(Main.pathToTemperatureFile, "UTF-8");
            writer.println(status);
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }

    public static void setLocation(Location location){
        // Creates writer for file
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(Main.pathToLocationFile, "UTF-8");
            writer.println(location.getInput());
            writer.println(location.getPlaceId());
            writer.println(location.getLat());
            writer.println(location.getLon());
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }
}
