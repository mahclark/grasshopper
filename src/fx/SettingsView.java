package fx;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SettingsView extends View {
    private Stage stage;
    private Scene scene;
    private Button close;

    public SettingsView (Stage stage){
        this.stage=stage;}

    public void makeScene(){
        close = new Button();
        close.setOnMousePressed(e -> Main.getViews().get(ViewName.INITIAL).show());

        Pane root = new Pane();

        root.getChildren().add(close);
        close.setLayoutX(350);

        Label label = new Label("Settings");
        label.setFont(new Font(32));
        root.getChildren().add(label);
        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);

        TextField location = new TextField();
        location.setPromptText("Enter a location");
        root.getChildren().add(location);
        location.setLayoutX(50);
        location.setLayoutY(200);

        ChoiceBox timeformat = new ChoiceBox(FXCollections.observableArrayList("24Hr","12Hr"));
        timeformat.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change the time display style
                        Main.changetimeformat(t1);
                    }
                }
        );
        root.getChildren().add(timeformat);
        timeformat.setLayoutX(50);
        timeformat.setLayoutY(400);

        ChoiceBox notification = new ChoiceBox(FXCollections.observableArrayList("No","Yes"));
        notification.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        //Change notification property
                        Main.changenotistatus(t1);
                    }
                }
        );
        root.getChildren().add(notification);
        notification.setLayoutX(50);
        notification.setLayoutY(500);
    }

    public void show(){
        makeScene();
        stage.setScene(scene);
    }
}
