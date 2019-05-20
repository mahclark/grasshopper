package fx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import org.controlsfx.control.Notifications;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventPanel extends Pane {

    public EventPanel() {
        setPrefWidth(Main.screenWidth);
        setPrefHeight(Main.screenHeight);
        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Events");
        title.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 32));
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
        Label fromword = new Label("From:");
        fromword.setTextFill(Color.WHITE);
        fromword.setLayoutX(30);
        fromword.setLayoutY(297);
        Spinner from = new Spinner();
        from.setEditable(true);
        from.setValueFactory(new SpinnerValueFactory() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
            }
            @Override
            public void decrement(int i) {
                if (getValue()==null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(i));
                }
            }

            @Override
            public void increment(int i) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(i));
                }
            }
        });
        from.setLayoutX(70);
        from.setLayoutY(295);
        from.setPrefWidth(100);
        Label toword = new Label("To:");
        toword.setTextFill(Color.WHITE);
        toword.setLayoutX(190);
        toword.setLayoutY(297);
        Spinner to = new Spinner();
        to.setEditable(true);
        to.setValueFactory(new SpinnerValueFactory() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
            }
            @Override
            public void decrement(int i) {
                if (getValue()==null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(i));
                }
            }

            @Override
            public void increment(int i) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(i));
                }
            }
        });
        to.setLayoutX(230);
        to.setLayoutY(295);
        to.setPrefWidth(100);

        Text description = new Text("Description");
        description.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 20));
        description.setFill(Color.WHITE);
        description.setLayoutX(30);
        description.setLayoutY(380);
        TextField descriptionbox = new TextField();
        descriptionbox.setPromptText("Enter a brief description of the event");
        descriptionbox.setLayoutX(30);
        descriptionbox.setLayoutY(395);
        descriptionbox.setPrefSize(310,130);
        descriptionbox.setAlignment(Pos.TOP_LEFT);

        getChildren().add(eventtitle);
        getChildren().add(eventtitlebox);
        getChildren().add(eventdate);
        getChildren().add(eventdatepicker);
        getChildren().add(eventtime);
        getChildren().add(from);
        getChildren().add(fromword);
        getChildren().add(to);
        getChildren().add(toword);
        getChildren().add(description);
        getChildren().add(descriptionbox);

        Button addEvent = new Button("Add Event");
        addEvent.setLayoutX(Main.screenWidth-100);
        addEvent.setLayoutY(560);
        addEvent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ((InitialView) Main.getViews().get(ViewName.INITIAL)).toggleEventPanel();
                ((HourlyView) Main.getViews().get(ViewName.HOURLY)).toggleEventPanel();
                Notifications notif = Notifications.create()
                        .title("Event Created")
                        .text("Event Title:"+eventtitlebox.getText())
                        .graphic(null)
                        .hideAfter(Duration.seconds(2))
                        .position(Pos.BOTTOM_RIGHT);
                if (Main.getnotifstatus()){
                    notif.show();
                }
            }
            });
        getChildren().add(addEvent);
    }

    private void clicked() {
        ((InitialView) Main.getViews().get(ViewName.INITIAL)).toggleEventPanel();
        ((HourlyView) Main.getViews().get(ViewName.HOURLY)).toggleEventPanel();
    }
}
