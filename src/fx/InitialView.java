package fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InitialView extends View {

    private Stage stage;
    private Scene scene;

    private Pane root = new Pane();
    private Pane mainPane = new Pane();

    private EventPanel eventPanel = Main.eventPanel;
    private SettingsPanel settingsPanel = Main.settingsPanel;
    private boolean eventShowing = false;
    private boolean settingsShowing = false;

    public InitialView(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    private void makeScene(boolean withAnimation) {
        root.getChildren().clear();
        mainPane.getChildren().clear();
        root.getChildren().add(mainPane);

        Label mainTemp = new Label("15°");
        mainTemp.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 150));
        mainTemp.setLayoutX(0);
        mainTemp.setLayoutY(80);
        mainPane.getChildren().add(mainTemp);

        Graph graph = Main.temperatureGraph;
        mainPane.getChildren().add(graph);

        Selector selector = Main.selector;
        mainPane.getChildren().add(selector);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> // Main.getViews().get(ViewName.SETTINGS).show()
                callSettings());
        mainPane.getChildren().add(settingsButton);
        settingsButton.setLayoutX(10);
        settingsButton.setLayoutY(Main.screenHeight - 90);

        root.getChildren().add(eventPanel);
        eventPanel.setLayoutX(0);
        eventPanel.setLayoutY(Main.screenHeight - 50);

        root.getChildren().add(settingsPanel);
        settingsPanel.setLayoutX(-Main.screenWidth+1);
        settingsPanel.setLayoutY(82);

        if (withAnimation) {
            Animator.fade(mainTemp, 0.0, 1.0, 0.5);
            Animator.transitionTo(graph, 0, 270, 0.2);
            Animator.transitionTo(selector, 0, 20, 0.2);

            settingsButton.setLayoutY(Main.screenHeight + 10);
            Animator.transitionBy(settingsButton, 0, -100, 0.2);
        }

        scene.setOnMouseReleased(e -> selector.mouseUp());
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

    public void callSettings(){
        GaussianBlur blur = new GaussianBlur(0);
        Animator.timeline(blur.radiusProperty(), 8, 0.5);
        mainPane.setEffect(blur);
        Animator.transitionTo(settingsPanel, Main.screenWidth+30, 0, 0.5);
    }

    public void recallSettings(){
        GaussianBlur blur = new GaussianBlur(8);
        Animator.timeline(blur.radiusProperty(), 0, 0.5);
        mainPane.setEffect(blur);
        Animator.transitionTo(settingsPanel, -Main.screenWidth-30, 0, 0.5);
    }

    @Override
    public void show() {
        makeScene(stage.getScene() == null || stage.getScene() != scene);
                        // This is called every time because shared objects can't be on multiple views at once
                        // Perhaps there should be an initScene() and a loadSharedObjects()??
        stage.setScene(scene);
    }
}
