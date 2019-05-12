package fx;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    public final static int screenWidth = 562;
    public final static int screenHeight = 1000;  // 0.75 scaled resolution of iPhone 8

    private static Map<ViewName, View> views = new HashMap<>();

    public static Button sharedButton;

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);

        sharedButton = new Button("Shared Button");


        views.put(ViewName.INITIAL, new InitialView(stage));
        views.put(ViewName.HOURLY, new HourlyView(stage));

        views.get(ViewName.INITIAL).show();
        stage.setTitle("Grasshopper");
        stage.show();
    }

    public static Map<ViewName, View> getViews() {
        return views;
    }

    public static void main(String[] args) {
        launch(args);
    }
}