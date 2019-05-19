package fx;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;

import java.util.List;

public class HourlyView extends View {

    private Stage stage;
    private Scene scene;

    private Pane root = new Pane();
    private Pane mainPane = new Pane();

    private EventPanel eventPanel = Main.eventPanel;
    private boolean eventShowing = false;

    private Label strategyLbl = new Label();
    private Label weatherLbl = new Label();
    private Label eventInfoLbl = new Label();
    private Selector selector = Main.selector;
    private Graph graph = Main.temperatureGraph;
    private StrategyGenerator generator;

    public HourlyView(Stage stage) {
        this.stage = stage;

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    private void makeScene() {
        root.getChildren().clear();
        mainPane.getChildren().clear();
        root.getChildren().add(mainPane);

        mainPane.getChildren().add(graph);

        mainPane.getChildren().add(selector);
        Animator.transitionTo(selector, 0, 20, 0.2);

        root.getChildren().add(eventPanel);
        eventPanel.setLayoutX(0);
        eventPanel.setLayoutY(Main.screenHeight - 50);

        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    public void showHourlyWeather(int hour) {
        weatherLbl.setVisible(true);
        strategyLbl.setVisible(false);
        eventInfoLbl.setVisible(false);

        int date = selector.getSelectedDate();
        weatherLbl.setText("Weather info for " + hour + " " + date + " goes here");

        weatherLbl.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 21));
        weatherLbl.setWrapText(true);
        weatherLbl.setLayoutX(10);
        weatherLbl.setLayoutY(320);
        weatherLbl.setPrefWidth(Main.screenWidth - 20);
        mainPane.getChildren().add(weatherLbl);

        Animator.fade(weatherLbl, 0.0, 1.0, 0.5);
        Animator.transitionTo(graph, 0, 80, 0.2);
    }

    public void showStrategy(Event event) {
        weatherLbl.setVisible(false);
        strategyLbl.setVisible(true);
        eventInfoLbl.setVisible(true);

        eventInfoLbl.setText(event.getStartHour() + ":00 - " + event.getLocationName() + ", " + event.getOvers() + " overs");
        eventInfoLbl.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 28));
        eventInfoLbl.setLayoutY(72);
        mainPane.getChildren().add(eventInfoLbl);

        try {
            generator = new StrategyGenerator(new LocationWeatherOWM(event.getLocation()), event.getDate(), event.getStartHour(), event.getOvers());
            strategyLbl.setText(generator.getOutput());
        } catch (Exception e) {
            strategyLbl.setText("There is no weather information for the selected date at the moment.");
        }

        strategyLbl.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 21));
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
        return generator.getRelevantWeatherHours();
    }

    public void toggleEventPanel() {
        if (!eventShowing) {
            eventShowing = true;

            GaussianBlur blur = new GaussianBlur(0);
            Animator.timeline(blur.radiusProperty(), 8, 0.5);
            mainPane.setEffect(blur);

            Animator.transitionBy(eventPanel, 0, 100 - Main.screenHeight, 0.5);
        } else {
            eventShowing = false;

            GaussianBlur blur = new GaussianBlur(8);
            Animator.timeline(blur.radiusProperty(), 0, 0.5);
            mainPane.setEffect(blur);

            Animator.transitionBy(eventPanel, 0, Main.screenHeight - 100, 0.5);
        }
    }
    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
