package fx;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        Pane pane = new Pane();
        pane.getChildren().add(switchButton);

        TranslateTransition buttonTransition = new TranslateTransition();
        buttonTransition.setDuration(Duration.seconds(0.2));
        buttonTransition.setToX(20);
        buttonTransition.setToY(50);
        buttonTransition.setNode(switchButton);
        buttonTransition.play();

        BorderPane root = new BorderPane();
        root.setLeft(pane);
        Label label = new Label("Initial View");
        label.setFont(new Font(32));
        root.setTop(label);

        Button closeButton = new Button("Close");
        closeButton.setOnMousePressed(e -> stage.close());
        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10));
        bbar.getButtons().add(closeButton);
        root.setBottom(bbar);
        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void show() {
        makeScene();    // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
