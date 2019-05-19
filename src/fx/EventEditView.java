package fx;

import backend.Location;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.LocationWeatherOWM;

import java.util.List;

public class EventEditView extends View {
    private Stage stage;
    private Scene scene;

    private Pane root = new Pane();

    public EventEditView(Stage stage){
        this.stage = stage;
        this.scene = new Scene(root, Main.screenWidth, Main.screenHeight);
    }

    public void makeScene(){
        root.getChildren().clear();
    }

    public void show(){
        makeScene();
        stage.setScene(scene);
    }
}
