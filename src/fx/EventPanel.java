package fx;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;

public class EventPanel extends VBox {

    public boolean isShowing = false;
    private final int inputIndent = 125;

    private int editorHeight = 265;
    private int eventsHeight = Main.events.size()*65 + 60;

    private Button newEventBtn;
    private Label errorLbl;

    private TextField nameField;
    private ComboBox locationChoice;
    private DatePicker datePicker;
    private ComboBox<Integer> timeChoice;
    private ComboBox<Integer> overChoice;

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
            Text eventTxt = new Text(event.getName());
            eventTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
            eventTxt.setFill(Color.WHITE);
            eventTxt.setLayoutX(0);
            eventTxt.setLayoutY(20);

            Button editBtn = new Button("Edit");
            editBtn.setLayoutX(Main.screenWidth - 60);
            editBtn.setLayoutY(0);

            Pane cell = new Pane();
            cell.setPrefWidth(Main.screenWidth - 20);
            cell.getChildren().addAll(eventTxt, editBtn);
            getChildren().add(cell);

            Line lineBreak = new Line(10,0,Main.screenWidth - 10,0);
            lineBreak.setStroke(Color.WHITE);
            getChildren().add(lineBreak);
        }

        newEventBtn = new Button("New Event");
        newEventBtn.setOnAction(e -> newEvent());
        newEventBtn.setLayoutX(0);
        newEventBtn.setLayoutY(0);

        errorLbl = new Label();
        errorLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        errorLbl.setTextFill(Color.color(0.9,0.35,0.35));
        errorLbl.setOpacity(0.0);
        errorLbl.setPrefWidth(Main.screenWidth - 20);
        errorLbl.setAlignment(Pos.CENTER);
        errorLbl.setLayoutX(0);
        errorLbl.setLayoutY(0);
        errorLbl.toBack();

        Pane cell = new Pane();
        cell.getChildren().addAll(errorLbl, newEventBtn);
        getChildren().add(cell);

        Text nameTxt = new Text("Event Name:");
        nameTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        nameTxt.setFill(Color.WHITE);

        nameField = new TextField();
        nameField.setPromptText("Enter the title of event");
        nameField.setPrefWidth(230);

        addCell(nameTxt, nameField);

        Text locationTxt = new Text("Location:");
        locationTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        locationTxt.setFill(Color.WHITE);

        //TODO: Make this work
        locationChoice = new ComboBox<String>(
                //TODO:Change to some other things later
                FXCollections.observableArrayList());
        locationChoice.setEditable(true);
        locationChoice.setValue(Main.getUserLocation().getInput());
        TextFields.bindAutoCompletion(locationChoice.getEditor(),locationChoice.getItems());

        addCell(locationTxt, locationChoice);

        Text dateTxt = new Text("Date:");
        dateTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        dateTxt.setFill(Color.WHITE);
        datePicker = new DatePicker();
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        datePicker.setValue(LocalDate.now().plusDays(1));
        datePicker.setEditable(false);

        addCell(dateTxt, datePicker);

        Text timeTxt = new Text("Start Hour:");
        timeTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        timeTxt.setFill(Color.WHITE);

        timeChoice = new ComboBox<>();
        for (int i = 0; i < 23; i++) {
            timeChoice.getItems().add(i);
        }
        timeChoice.setValue(12);

        addCell(timeTxt, timeChoice);

        Text overTxt = new Text("No. of Overs:");
        overTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        overTxt.setFill(Color.WHITE);

        overChoice = new ComboBox<>();
        for (int i = 0; i <= 60; i += 5) {
            overChoice.getItems().add(i);
        }
        overChoice.setValue(20);

        addCell(overTxt, overChoice);

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
        Animator.transitionTo(this, 0, -eventsHeight - editorHeight, 0.3);
        Animator.fade(newEventBtn, 1.0, 0.0, 0.3);
        newEventBtn.setDisable(true);
    }

    private void addEvent() {
        boolean valid = false;
        if (nameField.getText().equals("")) {
            errorLbl.setText("Please choose a name");
        } else if (locationChoice.getValue().equals("")) { //TODO: Check for valid location
            errorLbl.setText("Please choose a location");
        } else if (datePicker.getValue() == null) {
            errorLbl.setText("Please choose a date");
        } else if (timeChoice.getValue() == null) {
            errorLbl.setText("Please choose a time");
        } else if (overChoice.getValue() == null) {
            errorLbl.setText("Please choose number of overs");

        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            errorLbl.setText("Date must be in the future");
        } else if ((int) overChoice.getValue() < 1) {
            errorLbl.setText("Overs cannot be negative");
        } else {
            valid = true;
        }

        if (valid) {
            Animator.fade(errorLbl, errorLbl.getOpacity(), 0.0, 0.5);
            toggle();
            Notifications notif = Notifications.create()
                    .title("Event Created")
//                .text("Event Title:"+nameField.getText())
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.BOTTOM_RIGHT);
            if (Main.getnotifstatus()) {
                notif.show();
            }
        } else {
            Animator.fade(errorLbl, 0.0, 1.0, 0.5);
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
            Main.selector.setDisable(false);
            Main.temperatureGraph.setDisable(false);

            GaussianBlur blur = new GaussianBlur(8);
            Animator.timeline(blur.radiusProperty(), 0, 0.5);
            ((InitialView) Main.getViews().get(ViewName.INITIAL)).blur(blur);
            ((HourlyView) Main.getViews().get(ViewName.HOURLY)).blur(blur);

            Animator.fade(errorLbl, errorLbl.getOpacity(), 0.0, 0.5);
            Animator.fade(newEventBtn, 0.0, 1.0, 0.3);
            newEventBtn.setDisable(false);

            Animator.transitionTo(this, 0, 0,0.5);
        }
    }
}
