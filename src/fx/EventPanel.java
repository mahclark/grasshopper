package fx;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EventPanel extends Pane {
    public EventPanel() {
        setPrefWidth(Main.screenWidth);
        setPrefHeight(Main.screenHeight);
        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Events");
        title.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 32));
        title.setTextFill(Color.WHITE);
        title.setPrefWidth(Main.screenWidth - 20);
        title.setLayoutX(5);
        title.setLayoutY(5);
        getChildren().add(title);

        title.setOnMouseClicked(e -> clicked());

        Text eventtitle = new Text("Event Title");
        eventtitle.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 20));
        eventtitle.setFill(Color.WHITE);
        eventtitle.setLayoutX(30);
        eventtitle.setLayoutY(80);
        TextField eventtitlebox = new TextField();
        eventtitlebox.setPromptText("Enter the title of event");
        eventtitlebox.setLayoutX(30);
        eventtitlebox.setLayoutY(95);

        Text eventdate = new Text("Event Date");
        eventdate.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 20));
        eventdate.setFill(Color.WHITE);
        eventdate.setLayoutX(30);
        eventdate.setLayoutY(180);
        DatePicker eventdatepicker = new DatePicker();
        eventdatepicker.setLayoutX(30);
        eventdatepicker.setLayoutY(195);

        Text eventtime = new Text("Event Time");
        eventtime.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 20));
        eventtime.setFill(Color.WHITE);
        eventtime.setLayoutX(30);
        eventtime.setLayoutY(280);
        Text description = new Text("Description");
        description.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 20));
        description.setFill(Color.WHITE);
        description.setLayoutX(30);
        description.setLayoutY(380);
        getChildren().add(eventtitle);
        getChildren().add(eventtitlebox);
        getChildren().add(eventdate);
        getChildren().add(eventdatepicker);
        getChildren().add(eventtime);
        getChildren().add(description);

        Button addEvent = new Button("Add Event");
        addEvent.setLayoutX(Main.screenWidth-100);
        addEvent.setLayoutY(560);
        getChildren().add(addEvent);
    }

    private void clicked() {
        ((InitialView) Main.getViews().get(ViewName.INITIAL)).toggleEventPanel();
        ((HourlyView) Main.getViews().get(ViewName.HOURLY)).toggleEventPanel();
    }
}
