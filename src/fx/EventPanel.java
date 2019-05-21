package fx;

import backend.EventFunctions;
import backend.Location;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;

public class EventPanel extends VBox {

    public boolean isShowing = false;
    private final int inputIndent = 125;

    private int editorHeight = 270;
    private int eventsHeight;

    private Button newEventBtn;
    private Label errorLbl;
    private Button deleteBtn;

    private TextField nameField;
    private ComboBox locationChoice;
    private DatePicker datePicker;
    private ComboBox<Integer> timeChoice;
    private ComboBox<Integer> overChoice;

    private UserEvent editingEvent;

    public EventPanel() {
        super(20);
        reformat();
    }
    private void reformat() {
        getChildren().clear();
        setPrefWidth(Main.screenWidth);
        setPadding(new Insets(10, 10, 100, 10));

        editingEvent = null;

        eventsHeight = Math.min(Main.events.size()*45, 300) + 70;

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Events");
        title.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 32));
        title.setTextFill(Color.WHITE);
        title.setPrefWidth(Main.screenWidth - 20);
        title.setLayoutX(5);
        title.setLayoutY(5);
        getChildren().add(title);

        title.setOnMouseClicked(e -> toggle());

        ScrollPane scrollPane = new ScrollPane();
        Pane scrollContent = new VBox(10);
        scrollPane.setContent(scrollContent);
        scrollPane.setMinHeight(0);
        scrollPane.setMaxHeight(300);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add("fx/scrollPane.css");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //Hides scroll bars
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(0);      //Prevents horizontal scrolling
        scrollPane.setHmin(0);
        getChildren().add(scrollPane);

        for (UserEvent event : Main.events) {
            Text eventTxt = new Text(event.getName());
            eventTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
            eventTxt.setFill(Color.WHITE);
            eventTxt.setLayoutX(0);
            eventTxt.setLayoutY(20);

            Button editBtn = new Button("Edit");
            editBtn.setLayoutX(Main.screenWidth - 80);
            editBtn.setLayoutY(0);

            editBtn.setOnAction(e -> editEvent(event));

            Pane cell = new Pane();
            cell.getChildren().addAll(eventTxt, editBtn);
            scrollContent.getChildren().add(cell);

            Line lineBreak = new Line(10,0,Main.screenWidth - 40,0);
            lineBreak.setStroke(Color.WHITE);
            scrollContent.getChildren().add(lineBreak);
        }

        newEventBtn = new Button("New UserEvent");
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

        Text nameTxt = new Text("UserEvent Name:");
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
        locationChoice = new ComboBox<String>();
        locationChoice.setEditable(true);
        //locationChoice.setValue(Main.getUserLocation().getInput());
        locationChoice.getEditor().setText(Main.getUserLocation().getInput());
        try {
            TextFields.bindAutoCompletion(locationChoice.getEditor(), Location.getLocation(locationChoice.getEditor().getText(), true));
        }
        catch (IOException e){}
        locationChoice.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    TextFields.bindAutoCompletion(locationChoice.getEditor(), Location.getLocation(locationChoice.getEditor().getText(), true));
                }
                catch (IOException e){}
            }
        });


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

        //Making the Date Picker's popup use a PopOver, for easier manipulation
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);

        Node datePickerPopupContent = datePickerSkin.getPopupContent();

        datePicker.setOnShown(e -> {
            PopOver datePickerPopover  = new PopOver();
            datePickerPopover.setAutoFix(true);
            datePickerPopover.setDetachable(false);
            datePickerPopover.setAutoHide(true);
            datePickerPopover.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
            datePickerPopover.setContentNode(datePickerPopupContent);
            datePickerPopover.show(datePicker);

        });

        datePicker.setSkin(datePickerSkin);
        addCell(dateTxt, datePicker);   //End of setting up the date picker

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

        Button addEvent = new Button("Save UserEvent");
        addEvent.setOnAction(e -> addEvent());

        deleteBtn = new Button("Cancel");
        deleteBtn.setOnAction(e -> deleteEvent());
        deleteBtn.setLayoutX(Main.screenWidth - 70);

        Pane btnPane = new Pane();
        btnPane.getChildren().addAll(addEvent, deleteBtn);
        getChildren().add(btnPane);
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

    private void deleteEvent() {
        if (editingEvent != null) {
            Main.events.remove(editingEvent);
        }

        Notifications notif = Notifications.create()
                .title("UserEvent Deleted")
//                .text("UserEvent Title:"+nameField.getText())
                .graphic(null)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT);
        if (Main.getnotifstatus()) {
            notif.show();
        }

        resetView();
    }

    private void editEvent(UserEvent event) {
        editingEvent = event;

        deleteBtn.setText("Delete");

        Animator.transitionTo(this, 0, -eventsHeight - editorHeight, 0.3);
        Animator.fade(newEventBtn, newEventBtn.getOpacity(), 0.0, 0.3);
        newEventBtn.setDisable(true);

        nameField.setText(event.getName());
        locationChoice.setValue(event.getLocation().getInput());

        datePicker.setValue(LocalDate.of((event.getDate() - event.getDate() % 10000)/10000, (event.getDate() % 10000  - event.getDate() % 100)/100, event.getDate() % 100));
        timeChoice.setValue(event.getStartHour());
        overChoice.setValue(event.getOvers());
    }

    private void newEvent() {
        editingEvent = null;

        deleteBtn.setText("Cancel");

        Animator.transitionTo(this, 0, -eventsHeight - editorHeight, 0.3);
        Animator.fade(newEventBtn, newEventBtn.getOpacity(), 0.0, 0.3);
        newEventBtn.setDisable(true);
    }

    private void addEvent() {
        boolean valid = false;
        if (nameField.getText().equals("")) {
            errorLbl.setText("Please choose a name");
        } else if (locationChoice.getEditor().getText().equals("")) { //TODO: Check for valid location
            errorLbl.setText("Please choose a location");
        } else if (datePicker.getValue() == null) {
            errorLbl.setText("Please choose a date");
        } else if (timeChoice.getValue() == null) {
            errorLbl.setText("Please choose a time");
        } else if (overChoice.getValue() == null) {
            errorLbl.setText("Please choose number of overs");
        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            errorLbl.setText("Date must be in the future");
        } else if (overChoice.getValue() < 1) {
            errorLbl.setText("Overs cannot be negative");
        } else {
            valid = true;
        }

        if (valid) {
            Animator.fade(errorLbl, errorLbl.getOpacity(), 0.0, 0.5);

            int month = datePicker.getValue().getMonthValue();
            int day = datePicker.getValue().getDayOfMonth();
            String date = "" + datePicker.getValue().getYear() + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "") + day;

            UserEvent newEvent = new UserEvent(nameField.getText(), new Location(locationChoice.getEditor().getText()), Integer.parseInt(date), timeChoice.getValue(), overChoice.getValue());

            if (editingEvent != null) {
                Main.events.remove(editingEvent);
            }

            Main.events.add(newEvent);
            Main.events.sort(Comparator.comparingInt(UserEvent::getDate));

            resetView();

            Notifications notif = Notifications.create()
                    .title("UserEvent Saved")
//                .text("UserEvent Title:"+nameField.getText())
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

    private void resetView() {
        Main.selector = new Selector();

        saveEvents();
        toggle();
        reformat();

        Main.getViews().get(ViewName.INITIAL).show();
    }

    private void saveEvents() {
        // Saves events to file
        EventFunctions.saveEvents(Main.events);
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
