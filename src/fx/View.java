package fx;

import javafx.stage.Stage;
import javafx.scene.Scene;

public abstract class View {

    private Stage stage;
    private Scene scene;

    abstract void show();
}
