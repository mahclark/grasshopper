package fx;

import backend.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class SettingsPanel extends Pane {

    private Button close;
    private boolean nearme;

    public SettingsPanel(){
        setPrefWidth(300);
        setPrefHeight(500);
        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Settings");
        title.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 32));
        title.setTextFill(Color.WHITE);
        title.setPrefWidth(Main.screenWidth - 20);
        getChildren().add(title);
        title.setLayoutX(5);
        title.setLayoutY(5);

        close = new Button("X");
        //TODO: implement PREVIOUS VIEW
        close.setOnMousePressed(e -> clicked());

//        ChoiceBox timeformat = new ChoiceBox(FXCollections.observableArrayList("24Hr","12Hr"));
//        if (Main.gettimeformat()){
//            timeformat.setValue("12Hr");
//        }
//        else{
//            timeformat.setValue("24Hr");
//        }
//
//        timeformat.getSelectionModel().selectedIndexProperty().addListener(
//                new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                        //Change the time display style
//                        Main.changetimeformat(t1);
//                    }
//                }
//        );


        ChoiceBox notification = new ChoiceBox(FXCollections.observableArrayList("No","Yes"));
        if (Main.getnotifstatus()){
            notification.setValue("Yes");
        }
        else {
            notification.setValue("No");
        }

        notification.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change notification property
                        Main.changenotistatus(t1);
                    }
                }
        );

        getChildren().add(close);
        close.setLayoutX(270);
        close.setLayoutY(5);

        CheckBox nearmebox = new CheckBox("Near Me");
        nearmebox.setSelected(nearme);
        nearmebox.setTextFill(Color.WHITE);
        nearmebox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                nearme = t1;
            }
        });
        ComboBox location = new ComboBox<String>();
        location.setEditable(true);
        location.getEditor().setText(Main.getUserLocation().getInput());
        try {
            TextFields.bindAutoCompletion(location.getEditor(), Location.getLocation(location.getEditor().getText(), true));
        }
        catch (IOException e){}
        location.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    TextFields.bindAutoCompletion(location.getEditor(), Location.getLocation(location.getEditor().getText(), true));
                }
                catch (IOException e){}
            }
        });
        location.selectionModelProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                Main.setUserLocation(new Location(t1.toString()));
            }
        });

        getChildren().add(nearmebox);
        nearmebox.setLayoutX(30);
        nearmebox.setLayoutY(103);

        getChildren().add(location);
        location.setLayoutX(110);
        location.setLayoutY(100);

        Text l =new Text("Location");
        l.setFont(Font.font("Times New Roman",20));
        l.setFill(Color.WHITE);
        getChildren().add(l);
        l.setLayoutX(30);
        l.setLayoutY(85);

        Text temp = new Text("Temperature");
        temp.setFont(Font.font("Times New Roman",20));
        temp.setFill(Color.WHITE);
        getChildren().add(temp);
        temp.setLayoutX(30);
        temp.setLayoutY(235);
        ChoiceBox temperaturebox = new ChoiceBox(FXCollections.observableArrayList("Celsius","Fahrenheit"));
        temperaturebox.setLayoutX(30);
        temperaturebox.setLayoutY(250);
        if(!Main.gettempformat())
            temperaturebox.setValue("Celsius");
        else{
            temperaturebox.setValue("Fahrenheit");
        }
        temperaturebox.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change the time display style
                        Main.changetempformat(t1);
                    }
                }
        );
        getChildren().add(temperaturebox);

//        getChildren().add(timeformat);
//        timeformat.setLayoutX(50);
//        timeformat.setLayoutY(250);
//
//        Text t =new Text("Time Format");
//        t.setFont(Font.font("Times New Roman",20));
//        t.setFill(Color.WHITE);
//        getChildren().add(t);
//        t.setLayoutX(30);
//        t.setLayoutY(235);

        getChildren().add(notification);
        notification.setLayoutX(30);
        notification.setLayoutY(400);

        Text n =new Text("Notification");
        n.setFont(Font.font("Times New Roman",20));
        n.setFill(Color.WHITE);
        getChildren().add(n);
        n.setLayoutX(30);
        n.setLayoutY(385);
    }

    private void clicked() {
        ((InitialView) Main.getViews().get(ViewName.INITIAL)).recallSettings();
        ((HourlyView) Main.getViews().get(ViewName.HOURLY)).recallSettings();
    }
}

