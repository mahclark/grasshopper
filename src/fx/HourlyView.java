package fx;

import javafx.scene.Scene;
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

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
