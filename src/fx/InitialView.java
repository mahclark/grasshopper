package fx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(switchButton);

        BorderPane root = new BorderPane();
        root.setLeft(vbox);
        Label label = new Label("Initial View");
        label.setFont(new Font(32));
        root.setTop(label);

        Button closeButton = new Button("Close");
        closeButton.setOnMousePressed(e -> stage.close());
        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10));
        bbar.getButtons().add(closeButton);
        root.setBottom(bbar);
        Scene scene = new Scene(root, Main.screenWidth, Main.screenHeight);

        this.scene = scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void show() {
        makeScene();
        stage.setScene(scene);
    }
}
