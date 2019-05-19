package fx;

import backend.Location;
import com.sun.scenario.Settings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;

public class HourlyView extends View {

    private Stage stage;
    private Scene scene;

    public HourlyView(Stage stage) {
        this.stage = stage;
    }


//    @Override
    private void makeScene() {
        Pane root = new Pane();

        Label label = new Label("Hourly View");
        label.setFont(new Font(32));
        root.getChildren().add(label);

        Graph graph = Main.temperatureGraph;
        root.getChildren().add(graph);
        Animator.transitionTo(graph, 0, 110, 0.2);

        Selector selector = Main.selector;
        root.getChildren().add(selector);
        Animator.transitionTo(selector, 0, 50, 0.2);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> Main.getViews().get(ViewName.SETTINGS));
        root.getChildren().add(settingsButton);
        settingsButton.setLayoutX(10);
        settingsButton.setLayoutY(Main.screenHeight - 35);

        try {
            StrategyGenerator generator = new StrategyGenerator(new LocationWeatherOWM(new Location("Cambridge")), 20190518, 12, 30);
            Label strategyLbl = new Label(generator.getOutput());
            strategyLbl.setFont(new Font(18));
            strategyLbl.setWrapText(true);
            strategyLbl.setLayoutX(10);
            strategyLbl.setLayoutY(320);
            strategyLbl.setPrefWidth(Main.screenWidth - 20);
            root.getChildren().add(strategyLbl);

            Animator.fade(strategyLbl, 0.0, 1.0, 0.5);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);

        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
