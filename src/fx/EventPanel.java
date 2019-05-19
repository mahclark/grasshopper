package fx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EventPanel extends HBox {

    public EventPanel() {
        super(0);
        setPrefWidth(Main.screenWidth);
        setPrefHeight(Main.screenHeight);
        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Events");
        title.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 32));
        title.setTextFill(Color.WHITE);
        title.setPrefWidth(Main.screenWidth - 20);
        getChildren().add(title);

        title.setOnMouseClicked(e -> clicked());
    }

    private void clicked() {
        ((InitialView) Main.getViews().get(ViewName.INITIAL)).toggleEventPanel();
        ((HourlyView) Main.getViews().get(ViewName.HOURLY)).toggleEventPanel();
    }
}
