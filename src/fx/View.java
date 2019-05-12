package fx;

import javafx.stage.Stage;
import javafx.scene.Scene;

public abstract class View {

    private Stage stage;
    private Scene scene;

    abstract void makeScene();
    abstract Scene getScene();
    abstract void show();
}
