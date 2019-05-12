package fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HourlyView extends View {

    private Stage stage;
    private Scene scene;

    public HourlyView(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void makeScene() {
        Button switchButton = Main.sharedButton;
        switchButton.setOnMousePressed(e -> Main.getViews().get(ViewName.INITIAL).show());

        Pane root = new Pane();

        root.getChildren().add(switchButton);
        Animator.transition(switchButton, 70, 50, 0.2);

        Label label = new Label("Hourly View");
        label.setFont(new Font(32));
        root.getChildren().add(label);

        Graph graph = Main.temperatureGraph;
        root.getChildren().add(graph);

        Animator.transition(graph, 0, 100, 0.2);

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
