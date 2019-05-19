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

    private Pane root = new Pane();

    public InitialView(Stage stage) {
        this.stage = stage;

        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    private void makeScene(boolean withAnimation) {
        root.getChildren().clear();

        Label mainTemp = new Label("15°");
        mainTemp.setFont(new Font(150));
        mainTemp.setLayoutX(0);
        mainTemp.setLayoutY(50);
        root.getChildren().add(mainTemp);

        Graph graph = Main.temperatureGraph;
        root.getChildren().add(graph);

        Selector selector = Main.selector;
        root.getChildren().add(selector);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> Main.getViews().get(ViewName.SETTINGS).show());
        root.getChildren().add(settingsButton);
        settingsButton.setLayoutX(10);
        settingsButton.setLayoutY(Main.screenHeight - 35);

        if (withAnimation) {
            Animator.fade(mainTemp, 0.0, 1.0, 0.5);
            Animator.transitionTo(graph, 0, 270, 0.2);
            Animator.transitionTo(selector, 0, 20, 0.2);

            settingsButton.setLayoutY(Main.screenHeight + 10);
            Animator.transitionBy(settingsButton, 0, -45, 0.2);
        }

        scene.setOnMouseReleased(e -> selector.mouseUp());
    }

    @Override
    public void show() {
        makeScene(stage.getScene() == null || stage.getScene() != scene);
                        // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
