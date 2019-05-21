package fx;

import backend.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

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

        getChildren().add(locationChoice);
        locationChoice.setLayoutX(150);
        locationChoice.setLayoutY(40);

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
}

