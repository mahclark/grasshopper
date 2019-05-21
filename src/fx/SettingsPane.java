package fx;

import backend.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Window;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.stream.Stream;

public class SettingsPane extends Pane {

//    private Button close;
//    private boolean nearme;
    private Label title;
    private ChoiceBox notificationChoice;
//    private CheckBox nearmebox;
    private ComboBox locationChoice;


    public SettingsPane(){
        setPrefWidth(Main.screenWidth);
        setPrefHeight(200);
//        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: transparent");

        title = new Label("Settings");
        title.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 25));
        title.setLayoutX(5);
        title.setLayoutY(5);
        getChildren().add(title);

        locationChoice = new ComboBox<String>();
        locationChoice.setEditable(true);
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

        ComboBox<String> cmb = new ComboBox<>();
        cmb.setTooltip(new Tooltip());
        new ComboBoxAutoComplete<String>(cmb);
        getChildren().add(cmb);

        cmb.setLayoutX(150);
        cmb.setLayoutY(40);
        cmb.setPrefWidth(200);

        Text locationTxt =new Text("Location:");
        locationTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        getChildren().add(locationTxt);
        locationTxt.setLayoutX(15);
        locationTxt.setLayoutY(60);

        Text tempTxt = new Text("Unit:");
        tempTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        getChildren().add(tempTxt);
        tempTxt.setLayoutX(15);
        tempTxt.setLayoutY(100);
        ChoiceBox tempChoice = new ChoiceBox(FXCollections.observableArrayList("Celsius","Fahrenheit"));
        tempChoice.setLayoutX(150);
        tempChoice.setLayoutY(80);
        if(!Main.gettempformat())
            tempChoice.setValue("Celsius");
        else{
            tempChoice.setValue("Fahrenheit");
        }
        tempChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change the time display style
                        Main.changetempformat(t1);
                        Main.getViews().get(ViewName.INITIAL).show();
                        Main.temperatureGraph.reloadGraph();
                    }
                }
        );
        getChildren().add(tempChoice);

        Text notificationTxt =new Text("Notifications:");
        notificationTxt.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
        getChildren().add(notificationTxt);
        notificationTxt.setLayoutX(15);
        notificationTxt.setLayoutY(140);

        notificationChoice = new ChoiceBox(FXCollections.observableArrayList("No","Yes"));
        notificationChoice.setValue(Main.getnotifstatus() ? "Yes" : "No");

        notificationChoice.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change notificationChoice property
                        Main.changenotistatus(t1);
                    }
                }
        );

        getChildren().add(notificationChoice);
        notificationChoice.setLayoutX(150);
        notificationChoice.setLayoutY(120);
    }
    class ComboBoxAutoComplete<T> {

        private ComboBox<String> cmb;
        String filter = "";
        private ObservableList<String> originalItems;

        public ComboBoxAutoComplete(ComboBox<String> cmb) {
            this.cmb = cmb;
            originalItems = FXCollections.observableArrayList(cmb.getItems());
            cmb.setTooltip(new Tooltip());
            cmb.setOnKeyPressed(this::handleOnKeyPressed);
            cmb.setOnHidden(this::handleOnHiding);
        }

        public void handleOnKeyPressed(KeyEvent e) {
            try {
//                cmb.setValue(filter);
                ObservableList<String> list = FXCollections.observableArrayList(Location.getLocation(filter, true));
                cmb.setItems(list);
                originalItems = list;
            } catch (IOException exception) {
                System.out.println("No internet connection");
            }
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            KeyCode code = e.getCode();

            if (code.isLetterKey()) {
                filter += e.getText();
            }
            if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
                filter = filter.substring(0, filter.length() - 1);
            }
            cmb.getItems().setAll(originalItems);
            if (code == KeyCode.ESCAPE) {
                filter = "";
            }
            if (filter.length() == 0) {
                cmb.getTooltip().hide();
            } else {
                Stream<String> items = cmb.getItems().stream();
                String txtUsr = filter.toString().toLowerCase();
                items.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
                cmb.getTooltip().setText(txtUsr);
                Window stage = cmb.getScene().getWindow();
                double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
                double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
                cmb.getTooltip().show(stage, posX, posY);
                cmb.show();
            }
            try {
                ObservableList<String> list = FXCollections.observableArrayList(Location.getLocation(filter, true));
                cmb.setItems(list);
            } catch (IOException exception) {

            }
        }

        public void handleOnHiding(Event e) {
            if (cmb.getValue() != null) {
                Main.setUserLocation(new Location(cmb.getValue()));
                Main.getViews().get(ViewName.INITIAL).show();
                Main.temperatureGraph.reloadGraph();

                cmb.setValue(filter);
                filter = "";
                cmb.getTooltip().hide();
                String s = cmb.getSelectionModel().getSelectedItem();
                cmb.getSelectionModel().select(s);
            }
        }
    }

}
