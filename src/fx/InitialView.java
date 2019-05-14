package fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InitialView extends View {

    private Stage stage;
    private Scene scene;

    public InitialView(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void makeScene() {
        Button switchButton = Main.sharedButton;
        switchButton.setOnMousePressed(e -> Main.getViews().get(ViewName.HOURLY).show());

        Pane root = new Pane();

        root.getChildren().add(switchButton);
        Animator.transition(switchButton, 20, 600, 0.2);

        Label label = new Label("Initial View");
        label.setFont(new Font(32));
        root.getChildren().add(label);

        Graph graph = Main.temperatureGraph;
        root.getChildren().add(graph);
        Animator.transition(graph, 0, 300, 0.2);

        Selector selector = Main.selector;
        root.getChildren().add(selector);
        Animator.transition(selector, 0, 50, 0.2);

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    @Override
    public void show() {
        makeScene();    // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
