package fx;

import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animator {

    public static void transitionTo(Node node, int x, int y, double duration) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(duration));
        transition.setToX(x);
        transition.setToY(y);
        transition.setNode(node);

        transition.play();
    }

    public static void transitionBy(Node node, int x, int y, double duration) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(duration));
        transition.setByX(x);
        transition.setByY(y);
        transition.setNode(node);

        transition.play();
    }

    public static FadeTransition fade(Node node, double fromValue, double toValue, double duration) {
        FadeTransition transition = new FadeTransition(Duration.seconds(duration), node);
        transition.setFromValue(fromValue);
        transition.setToValue(toValue);

        transition.play();

        return transition;
    }

    public static void scale(Node node, double xScale, double yScale, double duration) {
        ScaleTransition transition = new ScaleTransition(Duration.seconds(duration), node);
        transition.setByX(xScale);
        transition.setByY(yScale);

        transition.play();
    }

    public static void timeline(WritableValue value, double toValue, double duration) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(value, toValue);
        KeyFrame kf = new KeyFrame(Duration.seconds(duration), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
}
