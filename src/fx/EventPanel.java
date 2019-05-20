package fx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import org.controlsfx.control.Notifications;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventPanel extends VBox {

    public boolean isShowing = false;
    private boolean editorShowing = false;
    private final int inputIndent = 125;
    private Button newEventBtn;
    private int editorHeight = 220;
    private int eventsHeight = Main.events.size()*80;

    public EventPanel() {
        super(20);
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

        for (Event event : Main.events) {
            Text eventText = new Text(event.getName());
            eventText.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
            eventText.setFill(Color.WHITE);
            eventText.setLayoutX(0);
            eventText.setLayoutY(20);

            Button editBtn = new Button("Edit");
            editBtn.setLayoutX(Main.screenWidth - 60);
            editBtn.setLayoutY(0);

            Pane cell = new Pane();
            cell.setPrefWidth(Main.screenWidth - 20);
            cell.getChildren().addAll(eventText, editBtn);
            getChildren().add(cell);

            Line lineBreak = new Line(10,0,Main.screenWidth - 10,0);
            lineBreak.setStroke(Color.WHITE);
            getChildren().add(lineBreak);
        }

        newEventBtn = new Button("New Event");
        newEventBtn.setOnAction(e -> newEvent());
        getChildren().add(newEventBtn);

        Text nameTxt = new Text("Event Name:");
        nameTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        nameTxt.setFill(Color.WHITE);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter the title of event");
        nameField.setPrefWidth(230);

        addCell(nameTxt, nameField);

        Text dateTxt = new Text("Event Date:");
        dateTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        dateTxt.setFill(Color.WHITE);
        DatePicker datePicker = new DatePicker();

        addCell(dateTxt, datePicker);

        Text timeTxt = new Text("Start Time:");
        timeTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        timeTxt.setFill(Color.WHITE);

        Spinner timeSpinner = new Spinner();
        timeSpinner.setEditable(true);
        timeSpinner.setValueFactory(new SpinnerValueFactory() {
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

        addCell(timeTxt, timeSpinner);

        Text overTxt = new Text("No. of Overs:");
        overTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        overTxt.setFill(Color.WHITE);

        Spinner overSpinner = new Spinner();
        overSpinner.setEditable(true);
        overSpinner.setValueFactory(new SpinnerValueFactory() {
            @Override
            public void decrement(int i) {
                if (getValue() == null) {
                    setValue(20);
                } else if ((int) getValue() > 5) {
                    setValue((int) getValue() - i*5);
                }
            }

            @Override
            public void increment(int i) {
                if (getValue() == null) {
                    setValue(20);
                } else {
                    setValue((int) getValue() + i*5);
                }
            }
        });

        addCell(overTxt, overSpinner);

        Button addEvent = new Button("Add Event");
        addEvent.setOnAction(e -> addEvent());
        getChildren().add(addEvent);
    }

    private void addCell(Node node1, Node node2) {
        Pane cell = new Pane();

        node1.setLayoutX(0);
        node1.setLayoutY(17);
        node2.setLayoutX(inputIndent);
        node2.setLayoutY(0);

        cell.setPrefWidth(Main.screenWidth - 20);
        cell.getChildren().addAll(node1, node2);
        getChildren().add(cell);
    }

    private void newEvent() {
        editorShowing = true;
        Animator.transitionTo(this, 0, -eventsHeight - editorHeight, 0.3);
        Animator.fade(newEventBtn, 1.0, 0.0, 0.3);
        newEventBtn.setDisable(true);
    }

    private void addEvent() {
        toggle();
        Notifications notif = Notifications.create()
                .title("Event Created")
//                .text("Event Title:"+nameField.getText())
                .graphic(null)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT);
        if (Main.getnotifstatus()){
            notif.show();
        }
    }

    private void clicked() {
        toggle();
    }

    public void toggle() {
        if (!isShowing) {
            isShowing = true;
            Main.selector.setDisable(true);
            Main.temperatureGraph.setDisable(true);

            GaussianBlur blur = new GaussianBlur(0);
            Animator.timeline(blur.radiusProperty(), 8, 0.5);
            ((InitialView) Main.getViews().get(ViewName.INITIAL)).blur(blur);
            ((HourlyView) Main.getViews().get(ViewName.HOURLY)).blur(blur);

            Animator.transitionTo(this, 0, -eventsHeight, 0.5);
        } else {
            isShowing = false;
            editorShowing = false;
            Main.selector.setDisable(false);
            Main.temperatureGraph.setDisable(false);

            GaussianBlur blur = new GaussianBlur(8);
            Animator.timeline(blur.radiusProperty(), 0, 0.5);
            ((InitialView) Main.getViews().get(ViewName.INITIAL)).blur(blur);
            ((HourlyView) Main.getViews().get(ViewName.HOURLY)).blur(blur);

            Animator.fade(newEventBtn, 0.0, 1.0, 0.3);
            newEventBtn.setDisable(false);

            Animator.transitionTo(this, 0, 0,0.5);
        }
    }
}
