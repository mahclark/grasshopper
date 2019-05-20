package fx;

import backend.Location;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

//    public final static int screenWidth = 562;
//    public final static int screenHeight = 1000;  // 0.75 scaled resolution of iPhone 8

    private final static String iconPath = "file:resources/rainhopper_512x512.png";

    public final static int screenWidth = 375;
    public final static int screenHeight = 667;  // 0.5 scaled resolution of iPhone 8

    private static boolean notification_status;//false=NO,true=YES
    private static boolean temperature_status;//false=C,true=F
    private static boolean time_format;//false=24hr,true=12Hr
    private static Location userLocation = new Location("Cambridge");

    private static Map<ViewName, View> views = new HashMap<>();

    public static Selector selector;
    public static Graph temperatureGraph;
    public static EventPanel eventPanel;
    public static SettingsPanel settingsPanel;

    private static Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setResizable(false);

        selector = new Selector();
        temperatureGraph = new Graph();
        eventPanel = new EventPanel();
        settingsPanel = new SettingsPanel();

        views.put(ViewName.INITIAL, new InitialView(stage));
        views.put(ViewName.HOURLY, new HourlyView(stage));

        views.get(ViewName.INITIAL).show();
        stage.setTitle("Grasshopper");
        stage.getIcons().add(new Image(iconPath));
        stage.show();
    }

    public static Point getMousePosition() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        p.x -= stage.getX();
        p.y -= stage.getY();
        return p;
    }

    public static Location getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(Location userLocation) {
        Main.userLocation = userLocation;
    }

    public static Map<ViewName, View> getViews() {
        return views;
    }

    public static boolean getnotifstatus(){
        return notification_status;
    }

    public static void changenotistatus(Number notistatus){
        if (notistatus.equals(0)){
            notification_status=false;
        }
        else{
            notification_status=true;
        }
    }
    public static boolean gettempformat(){
        return temperature_status;
    }

    public static void changetempformat(Number timeformat){
        if (timeformat.equals(0)){
            temperature_status=false;
        }
        else{
            temperature_status=true;
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}