package backend;

import java.io.*;
import java.util.ArrayList;

public class SettingsFunctions {
    public static boolean getNotificationStatus(){

        // Tries to get file with setting
        String filepath = "resources/notifications.txt";
        File file = new File(filepath);
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
        String filepath = "resources/temperature.txt";
        File file = new File(filepath);
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

    public static String getLocation(){

        // Tries to get file with setting
        String filepath = "resources/location.txt";
        File file = new File(filepath);
        if (!file.exists()){
            return "Cambridge";
        }

        // If file exists, reads, and if set for false, changes to false, otherwise defaults to true
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.readLine();
        }
        catch (IOException e){
            return "Cambridge";
        }
    }

    public static void setNotificationStatus(Boolean status){
        // Creates writer for file
        String filepath = "resources/notifications.txt";
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            writer.println(status);
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }

    public static void setTemperatureStatus(Boolean status){
        // Creates writer for file
        String filepath = "resources/temperature.txt";
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            writer.println(status);
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }

    public static void setLocation(Location location){
        // Creates writer for file
        String filepath = "resources/location.txt";
        try {
            // Writes value
            PrintWriter writer = new PrintWriter(filepath, "UTF-8");
            writer.println(location.getInput());
            writer.close();
        }
        catch (IOException e){
            throw new FileWriteError("Cannot write to file");
        }
    }
}
