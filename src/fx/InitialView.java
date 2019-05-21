package fx;

import backend.Location;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;
import weather.NoInternetConnection;
import weather.WeatherData;

import java.util.*;

public class InitialView extends View {

    private Stage stage;
    private Scene scene;

    public Label titleLbl;

    private Pane root = new Pane();
    private Pane mainPane = new Pane();

    private EventPanel eventPanel = Main.eventPanel;
    private SettingsPanel settingsPanel = Main.settingsPanel;
    private boolean settingsShowing = false;

    private LocationWeatherOWM weather;

    public InitialView(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
        eventPanel.setLayoutX(0);
        eventPanel.setLayoutY(Main.screenHeight - 50);

        root.setBackground(new Background(new BackgroundFill(Color.color(0.72, 0.8, 0.94), CornerRadii.EMPTY, Insets.EMPTY)));

        try {
            weather = new LocationWeatherOWM(Main.getUserLocation());
        } catch (NoInternetConnection noInternetConnection) {
            weather = null;
        }
    }

    private void makeScene(boolean withAnimation) {
        root.getChildren().clear();
        mainPane.getChildren().clear();
        root.getChildren().add(mainPane);

        Label mainTemp = new Label(Main.temperatureGraph.getAvgTemperature() + "°");
        if (Main.gettempformat()){
            mainTemp = new Label(Math.round(Integer.parseInt(Main.temperatureGraph.getAvgTemperature())*9.0/5)+32 + "°");
        }
        mainTemp.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 150));
        mainTemp.setLayoutX(0);
        mainTemp.setLayoutY(55);
        mainPane.getChildren().add(mainTemp);

        titleLbl = new Label();
        titleLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 40));
        titleLbl.setLayoutX(10 + Main.screenWidth/2.0);
        titleLbl.setLayoutY(140);
        titleLbl.setWrapText(true);
        titleLbl.setPrefWidth(Main.screenWidth/2.0);
        mainPane.getChildren().add(titleLbl);
        if (weather != null) {
            try {
                int date = Main.selector.getSelectedDate();
                int hour = weather.giveHours(date).get(weather.giveHours(date).size() / 2);
                WeatherData data = weather.giveData(date, hour);
                titleLbl.setText(data.getDescription());
            } catch (IndexOutOfBoundsException e) {
                titleLbl.setText("");
            }
        } else {
            try {
                weather = new LocationWeatherOWM(Main.getUserLocation());
            } catch (NoInternetConnection noInternetConnection) {
                weather = null;
            }
        }

        Graph graph = Main.temperatureGraph;
        mainPane.getChildren().add(graph);

        Selector selector = Main.selector;
        mainPane.getChildren().add(selector);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> // Main.getViews().get(ViewName.SETTINGS).show()
                callSettings());
//        mainPane.getChildren().add(settingsButton);
        settingsButton.setLayoutX(10);
        settingsButton.setLayoutY(Main.screenHeight - 90);

        SettingsPane settings = new SettingsPane();
        settings.setLayoutY(460);
        mainPane.getChildren().add(settings);

        root.getChildren().add(eventPanel);

        root.getChildren().add(settingsPanel);
        settingsPanel.setLayoutX(-Main.screenWidth+1);
        settingsPanel.setLayoutY(82);

        if (withAnimation) {
            Animator.fade(mainTemp, 0.0, 1.0, 0.5);
            Animator.fade(titleLbl, 0.0, 1.0, 0.5);
            Animator.fade(settings, 0.0, 1.0, 0.5);
            Animator.transitionTo(graph, 0, 250, 0.2);

            settingsButton.setLayoutY(Main.screenHeight + 10);
            Animator.transitionBy(settingsButton, 0, -100, 0.2);
        }

        mainPane.setOnMouseClicked(e -> handleBackgroundTap());
        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    private void handleBackgroundTap() {
        if (eventPanel.isShowing) eventPanel.toggle();
        if (settingsShowing) recallSettings();
    }

    public void blur(GaussianBlur blur) {
        mainPane.setEffect(blur);
    }

    public void callSettings(){
        GaussianBlur blur = new GaussianBlur(0);
        Animator.timeline(blur.radiusProperty(), 8, 0.5);
        mainPane.setEffect(blur);
        Animator.transitionTo(settingsPanel, Main.screenWidth+30, 0, 0.5);
    }

    public void recallSettings(){
        GaussianBlur blur = new GaussianBlur(8);
        Animator.timeline(blur.radiusProperty(), 0, 0.5);
        mainPane.setEffect(blur);
        Animator.transitionTo(settingsPanel, -Main.screenWidth-30, 0, 0.5);
    }

    @Override
    public void show() {
        makeScene(stage.getScene() == null || stage.getScene() != scene);
                        // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
