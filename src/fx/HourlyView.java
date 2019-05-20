package fx;

import backend.Location;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;
import weather.NoInternetConnection;
import weather.WeatherData;

import java.util.List;

public class HourlyView extends View {

    private Stage stage;
    private Scene scene;

    private Pane root = new Pane();
    private Pane mainPane = new Pane();

    private EventPanel eventPanel = Main.eventPanel;
    private SettingsPanel settingsPanel = Main.settingsPanel;
    private boolean settingsShowing = false;

    private Label strategyLbl = new Label();
    private Label weatherTitle = new Label();
    private Label weatherLbl = new Label();
    private Label eventInfoLbl = new Label();
    private Selector selector = Main.selector;
    private Graph graph = Main.temperatureGraph;
    private StrategyGenerator generator;

    private LocationWeatherOWM weather;

    public HourlyView(Stage stage) {
        this.stage = stage;
        eventPanel.setLayoutX(0);
        eventPanel.setLayoutY(Main.screenHeight - 50);

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);

        try {
            weather = new LocationWeatherOWM(Main.getUserLocation());
        } catch (NoInternetConnection noInternetConnection) {
            weather = null;
        }
    }

    private void makeScene() {
        root.getChildren().clear();
        mainPane.getChildren().clear();
        root.getChildren().add(mainPane);

        mainPane.getChildren().add(graph);

        selector = Main.selector;
        mainPane.getChildren().add(selector);

        root.getChildren().add(eventPanel);

        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    public void showHourlyWeather(int hour) {
        weatherLbl.setVisible(true);
        strategyLbl.setVisible(false);
        eventInfoLbl.setVisible(false);

        int date = selector.getSelectedDate();

        weatherTitle.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 50));
        weatherTitle.setWrapText(true);
        weatherTitle.setLayoutX(10);
        weatherTitle.setLayoutY(280);
        weatherTitle.setPrefWidth(Main.screenWidth - 20);
        mainPane.getChildren().add(weatherTitle);

        weatherLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 25));
        weatherLbl.setWrapText(true);
        weatherLbl.setLayoutX(10);
        weatherLbl.setLayoutY(340);
        weatherLbl.setPrefWidth(Main.screenWidth - 20);
        mainPane.getChildren().add(weatherLbl);

        if (weather != null) {
            try {
                WeatherData data = weather.giveData(date, hour);
                weatherTitle.setText(data.getWeather());
                weatherLbl.setText(
                                data.getDescription() + "\n\n" +
                                "Cloud cover:\t" + data.getClouds() + "%\n" +
                                "Wind speed:\t" + data.getWindSpeed() + " mph\n" +
                                "Humidity:\t" + data.getHumidity() + "%\n" +
                                "Pressure:\t\t" + data.getPressure() + " mb"
                );
            } catch (IndexOutOfBoundsException e) {
                weatherTitle.setText("");
                weatherLbl.setText("");
            }
        } else {
            try {
                weather = new LocationWeatherOWM(Main.getUserLocation());
            } catch (NoInternetConnection noInternetConnection) {
                weather = null;
            }
        }

        Animator.fade(weatherTitle, 0.0, 1.0, 0.5);
        Animator.fade(weatherLbl, 0.0, 1.0, 0.5);
        Animator.transitionTo(graph, 0, 80, 0.2);

        mainPane.setOnMouseClicked(e -> handleBackgroundTap());
    }

    public void showStrategy(Event event) {
        weatherLbl.setVisible(false);
        strategyLbl.setVisible(true);
        eventInfoLbl.setVisible(true);

        eventInfoLbl.setText(event.getStartHour() + ":00 - " + event.getLocationName() + ", " + event.getOvers() + " overs");
        eventInfoLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 28));
        eventInfoLbl.setLayoutY(72);
        eventInfoLbl.setPrefWidth(Main.screenWidth);
        eventInfoLbl.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(eventInfoLbl);

        try {
            generator = new StrategyGenerator(new LocationWeatherOWM(event.getLocation()), event.getDate(), event.getStartHour(), event.getOvers());
            strategyLbl.setText(generator.getOutput());
        } catch (Exception e) {
            strategyLbl.setText("There is no weather information for the selected date at the moment.");
        }

        strategyLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        strategyLbl.setWrapText(true);
        strategyLbl.setLayoutX(10);
        strategyLbl.setLayoutY(320);
        strategyLbl.setPrefWidth(Main.screenWidth - 20);
        mainPane.getChildren().add(strategyLbl);

        Animator.fade(eventInfoLbl, 0.0, 1.0, 0.5);
        Animator.fade(strategyLbl, 0.0, 1.0, 0.5);
        Animator.transitionTo(graph, 0, 110, 0.2);
    }

    public List<Integer> getRelevantWeatherHours() {
        if (generator != null) {
            return generator.getRelevantWeatherHours();
        } else {
            return null;
        }
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
        makeScene();
        stage.setScene(scene);
    }
}
