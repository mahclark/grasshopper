package fx;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Graph extends ScrollPane {

    private Pane content = new Pane();

    private int height = 200;

    private int rangeMin = 0;
    private int rangeMax = 24; //Index of the first value which won't be shown

//    double[] temperatures = new double[24];
    Double[] temperatures = {6.0,6.0,6.0,6.0,7.0,7.0,8.0,9.0,10.0,11.0,13.0,14.0,15.0,15.0,15.0,16.0,15.0,13.0,11.0,9.0,8.0,8.0,8.0,7.0};

    public Graph() {
        assert temperatures.length == 24;
        assert rangeMin >= 0 && rangeMax <= 24 && rangeMin < rangeMax;

        content.setBackground(new Background(new BackgroundFill(Color.color(0.57, 0.72, 0.96), CornerRadii.EMPTY, Insets.EMPTY)));

        List<Double> temps = Arrays.asList(temperatures);

        double min = Collections.min(temps);
        double max = Collections.max(temps);

        temps = temps.subList(rangeMin, rangeMax);

        double cellWidth = 40;
        int verticalPadding = 10; //Gap between max/min dots and pane top/bottom
        int dotRadius = (int) cellWidth/7;

        List<Double> dotCentersX = new ArrayList<>();
        List<Double> dotCentersY = new ArrayList<>();

        //Draw the dots
        for (int i = 0; i < temps.size(); i++) {
            Pane cell = new Pane();
            cell.setPrefSize(cellWidth, height);
            cell.setLayoutX(i*cellWidth);
            content.getChildren().add(cell);

            if (i % 2 == 0) {
                cell.setBackground(new Background(new BackgroundFill(Color.color(0.72, 0.8, 0.94), CornerRadii.EMPTY, Insets.EMPTY)));
            }

            Circle dot = new Circle(dotRadius, Color.WHITE);
            dot.relocate(
                    cellWidth/2 - dotRadius,
                    verticalPadding + (height - dotRadius*2 - verticalPadding*2)*(1 - (temps.get(i) - min)/(max - min))
            );
            cell.getChildren().add(dot);

            dotCentersX.add(cellWidth*(i + 0.5));
            dotCentersY.add(dotRadius + verticalPadding + (height - dotRadius*2 - verticalPadding*2)*(1 - (temps.get(i) - min)/(max - min)));
        }

        int lineGap = dotRadius*3; //Gap between dot centre and line end

        //Draw the lines
        for (int i = 0; i < temps.size() - 1; i++) {
            double dist = Math.sqrt(
                    Math.pow(dotCentersX.get(i) - dotCentersX.get(i + 1), 2) +
                    Math.pow(dotCentersY.get(i) - dotCentersY.get(i + 1), 2)
            );

            Line line = new Line(
                    dotCentersX.get(i) + Math.abs(dotCentersX.get(i) - dotCentersX.get(i + 1))*lineGap/dist,
                    dotCentersY.get(i) - (dotCentersY.get(i) - dotCentersY.get(i + 1))*lineGap/dist,
                    dotCentersX.get(i + 1) - Math.abs(dotCentersX.get(i) - dotCentersX.get(i + 1))*lineGap/dist,
                    dotCentersY.get(i + 1) + (dotCentersY.get(i) - dotCentersY.get(i + 1))*lineGap/dist
            );
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(dotRadius/2.0);
            content.getChildren().add(line);
        }

        setContent(content);
        setPannable(true);
        setPrefWidth(Main.screenWidth);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
