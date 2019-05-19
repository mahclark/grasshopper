package fx;

import backend.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

public class SettingsPanel extends Pane {

    private Button close;
    private boolean nearme;

    public SettingsPanel(){
        setPrefWidth(300);
        setPrefHeight(500);
        setPadding(new Insets(10,10,10,10));

        setStyle("-fx-background-color: rgba(40,40,40, 0.7); -fx-background-radius: 10;");

        Label title = new Label("Settings");
        title.setFont(Font.loadFont(Main.class.getResource("Kollektif.ttf").toExternalForm(), 32));
        title.setTextFill(Color.WHITE);
        title.setPrefWidth(Main.screenWidth - 20);
        getChildren().add(title);
        title.setLayoutX(5);
        title.setLayoutY(5);

        close = new Button("X");
        //TODO: implement PREVIOUS VIEW
        close.setOnMousePressed(e -> clicked());

        ChoiceBox timeformat = new ChoiceBox(FXCollections.observableArrayList("24Hr","12Hr"));
        if (Main.gettimeformat()){
            timeformat.setValue("12Hr");
        }
        else{
            timeformat.setValue("24Hr");
        }

        timeformat.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change the time display style
                        Main.changetimeformat(t1);
                    }
                }
        );


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
        ComboBox location = new ComboBox<String>(
                //TODO:Change to some other things later
                FXCollections.observableArrayList());
        location.setEditable(true);
        location.setValue(Main.getUserLocation().getInput());
        TextFields.bindAutoCompletion(location.getEditor(),location.getItems());
        location.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observableValue, Object o, Object t1) {
                        Main.setUserLocation(new Location(t1.toString()));
                    }
                }
        );

        getChildren().add(nearmebox);
        nearmebox.setLayoutX(20);
        nearmebox.setLayoutY(103);

        getChildren().add(location);
        location.setLayoutX(100);
        location.setLayoutY(100);

        Text l =new Text("Location");
        l.setFont(Font.font("Times New Roman",20));
        l.setFill(Color.WHITE);
        getChildren().add(l);
        l.setLayoutX(30);
        l.setLayoutY(85);

        getChildren().add(timeformat);
        timeformat.setLayoutX(50);
        timeformat.setLayoutY(250);

        Text t =new Text("Time Format");
        t.setFont(Font.font("Times New Roman",20));
        t.setFill(Color.WHITE);
        getChildren().add(t);
        t.setLayoutX(30);
        t.setLayoutY(235);

        getChildren().add(notification);
        notification.setLayoutX(50);
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

