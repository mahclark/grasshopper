package fx;

import backend.EventFunctions;
import backend.Location;
import backend.SettingsFunctions;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.Point;
import java.awt.MouseInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    public final static int screenWidth = 375;
    public final static int screenHeight = 667;  // 0.5 scaled resolution of iPhone 8

    private static boolean notification_status;//false=NO,true=YES
    private static boolean temperature_status;//false=C,true=F
    private static boolean time_format;//false=24hr,true=12Hr
    private static Location userLocation = SettingsFunctions.getLocation();

    private static Map<ViewName, View> views = new HashMap<>();
    public static List<UserEvent> events = new ArrayList<>();

    public static final String resourcesFolderName      = "grasshopper_resources/";
    public static final String pathToEventFile          = resourcesFolderName+"event.txt";
    public static final String pathToLocationFile       = resourcesFolderName+"location.txt";
    public static final String pathToNotificationsFile  = resourcesFolderName+"notifications.txt";
    public static final String pathToTemperatureFile    = resourcesFolderName+"temperature.txt";
    public static final String pathToIcon               = resourcesFolderName+"rainhopper_512x512.png";
    public static final String[] pathsToResourcesToExtract = new String[]{
        pathToEventFile,
        pathToLocationFile,
        pathToNotificationsFile,
        pathToTemperatureFile,
        pathToIcon
    };

    public static Selector selector;
    public static Graph temperatureGraph;
    public static EventPanel eventPanel;
    public static SettingsPanel settingsPanel;

    private static Stage stage;

    @Override
    public void start(Stage stage){

        boolean resourcesExtractedOK = tryExtractResources();
        if (!resourcesExtractedOK) System.err.println("Couldn't extract resources from JAR, app may not run properly.");


        this.stage = stage;

        stage.setResizable(false);

        loadEvents();

        selector = new Selector();
        temperatureGraph = new Graph();
        eventPanel = new EventPanel();
        settingsPanel = new SettingsPanel();

        views.put(ViewName.INITIAL, new InitialView(stage));
        views.put(ViewName.HOURLY, new HourlyView(stage));

        views.get(ViewName.INITIAL).show();
        stage.setTitle("Grasshopper");
        stage.getIcons().add(new Image("file:"+pathToIcon));
        stage.show();
    }

    //Attempts to extract resources from JAR file where they don't already exist
    private boolean tryExtractResources() {
        try {
            for (String resourcePathString : pathsToResourcesToExtract){

                Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));
                Path pathToCopyTo = currentWorkingDir.resolve(resourcePathString);

                if(Files.exists(pathToCopyTo)){
                    System.out.println("Found file '" + resourcePathString + "', already in filesystem, skipping in extraction.");
                    continue;
                } else {
                    System.out.println("Extracting resource '"+resourcePathString+"' in JAR to file '"+pathToCopyTo+"' on filesystem.");

                    //Files.createFile() requires directory of file being created to exist
                    if (Files.notExists(pathToCopyTo.getParent())) {
                        Files.createDirectory(pathToCopyTo.getParent());
                    }
                    Files.createFile(pathToCopyTo); //Files.copy() requires destination file to exist

                    InputStream resourceInJAR = getClass().getResourceAsStream("/"+resourcePathString);
                    Files.copy(resourceInJAR, pathToCopyTo, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return true;
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Point getMousePosition() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        p.x -= stage.getX();
        p.y -= stage.getY();
        return p;
    }

    private void loadEvents() {
        events = EventFunctions.getEvents();
    }

    public static Location getUserLocation() {
        userLocation = SettingsFunctions.getLocation();
        return userLocation;
    }

    public static void setUserLocation(Location userLocation) {
        SettingsFunctions.setLocation(userLocation);
        Main.userLocation = userLocation;
    }

    public static Map<ViewName, View> getViews() {
        return views;
    }

    public static boolean getnotifstatus(){
        notification_status = SettingsFunctions.getNotificationStatus();
        return notification_status;
    }

    public static void changenotistatus(Number notistatus){
        if (notistatus.equals(0)){
            notification_status=false;
            SettingsFunctions.setNotificationStatus(false);
        }
        else{
            notification_status=true;
            SettingsFunctions.setNotificationStatus(true);
        }
    }
    public static boolean gettempformat(){
        temperature_status = SettingsFunctions.getTemperatureStatus();
        return temperature_status;
    }

    public static void changetempformat(Number timeformat){

        if (timeformat.equals(0)){
            temperature_status=false;
            SettingsFunctions.setTemperatureStatus(false);
        }
        else{
            temperature_status=true;
            SettingsFunctions.setTemperatureStatus(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}