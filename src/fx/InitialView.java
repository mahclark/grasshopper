package fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
        Pane root = new Pane();

        Label label = new Label("Initial View");
        label.setFont(new Font(32));
        root.getChildren().add(label);

        Graph graph = Main.temperatureGraph;
        root.getChildren().add(graph);
        Animator.transitionTo(graph, 0, 300, 0.2);

        Selector selector = Main.selector;
        root.getChildren().add(selector);
        Animator.transitionTo(selector, 0, 50, 0.2);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> Main.getViews().get(ViewName.SETTINGS).show());
        root.getChildren().add(settingsButton);
        settingsButton.setLayoutX(10);
        settingsButton.setLayoutY(Main.screenHeight + 10);
        Animator.transitionBy(settingsButton, 0, -45, 0.2);

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    @Override
    public void show() {
        makeScene();    // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
