package fx;

import backend.Location;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;

public class HourlyView extends View {

    private Stage stage;
    private Scene scene;

    private Pane root = new Pane();
    private Label strategyLbl = new Label();
    private Label weatherLbl = new Label();
    private Selector selector = Main.selector;

    public HourlyView(Stage stage) {
        this.stage = stage;

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }


//    @Override
    private void makeScene() {
        Label label = new Label("Hourly View");
        label.setFont(new Font(32));
        root.getChildren().add(label);

        Graph graph = Main.temperatureGraph;
        if (!root.getChildren().contains(graph)) root.getChildren().add(graph);
        Animator.transitionTo(graph, 0, 110, 0.3);

        if (!root.getChildren().contains(selector)) root.getChildren().add(selector);
        Animator.transitionTo(selector, 0, 50, 0.2);

        Event event = new Event("TestEvent", new Location("Cambridge"), 20190520, 12, 30);

        showStrategy(event);

        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    public void showHourlyWeather(int hour) {
        strategyLbl.setVisible(false);
        weatherLbl.setVisible(true);
        int date = selector.getSelectedDate();
        weatherLbl.setText("Weather info for " + hour + " " + date + " goes here");

        weatherLbl.setFont(new Font(18));
        weatherLbl.setWrapText(true);
        weatherLbl.setLayoutX(10);
        weatherLbl.setLayoutY(320);
        weatherLbl.setPrefWidth(Main.screenWidth - 20);
        if (!root.getChildren().contains(weatherLbl)) root.getChildren().add(weatherLbl);

        Animator.fade(weatherLbl, 0.0, 1.0, 0.5);
    }

    public void showStrategy(Event event) {
        weatherLbl.setVisible(false);
        strategyLbl.setVisible(true);
        try {
            StrategyGenerator generator = new StrategyGenerator(new LocationWeatherOWM(event.getLocation()), event.getDate(), event.getStartHour(), event.getOvers());
            strategyLbl.setText(generator.getOutput());
        } catch (Exception e) {
            System.out.println("weather exception: " + e.getMessage());
            strategyLbl.setText("There is no weather information for the selected date at the moment.");
        }

        strategyLbl.setFont(new Font(18));
        strategyLbl.setWrapText(true);
        strategyLbl.setLayoutX(10);
        strategyLbl.setLayoutY(320);
        strategyLbl.setPrefWidth(Main.screenWidth - 20);
        if (!root.getChildren().contains(strategyLbl)) root.getChildren().add(strategyLbl);

        Animator.fade(strategyLbl, 0.0, 1.0, 0.5);
    }

    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
