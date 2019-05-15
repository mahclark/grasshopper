package fx;

import javafx.animation.TranslateTransition;
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
}
