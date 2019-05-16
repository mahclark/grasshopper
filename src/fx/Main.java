package fx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

//    public final static int screenWidth = 562;
//    public final static int screenHeight = 1000;  // 0.75 scaled resolution of iPhone 8

    private final static String iconPath = "file:resources/rainhopper_4096x4096.png";

    public final static int screenWidth = 375;
    public final static int screenHeight = 667;  // 0.5 scaled resolution of iPhone 8

    private static Map<ViewName, View> views = new HashMap<>();

    public static Graph temperatureGraph;
    public static Selector selector;

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);

        temperatureGraph = new Graph();
        selector = new Selector();

        views.put(ViewName.INITIAL, new InitialView(stage));
        views.put(ViewName.HOURLY, new HourlyView(stage));
        views.put(ViewName.SETTINGS, new SettingsView(stage));

        views.get(ViewName.INITIAL).show();
        stage.setTitle("Grasshopper");
        stage.getIcons().add(new Image(iconPath));
        stage.show();
    }

    public static Map<ViewName, View> getViews() {
        return views;
    }

    public static void main(String[] args) {
        launch(args);
    }
}