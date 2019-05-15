package fx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Graph extends ScrollPane {

    private Pane content = new Pane();

    private int height = 200;
    final double cellWidth = 40;
    final int dotRadius = (int) cellWidth/7;

    private int rangeMin = 0;
    private int rangeMax = 24; //Index of the first value which won't be shown

    private GraphCell selectedCell; //null when nothing selected

//    double[] temperatures = new double[24];
    List<Double> temps = Arrays.asList(new Double[] {6.0,6.0,6.0,6.0,7.0,7.0,8.0,9.0,10.0,11.0,13.0,14.0,15.0,15.0,15.0,16.0,15.0,13.0,11.0,9.0,8.0,8.0,8.0,7.0});

    public Graph() {
        assert temps.size() == 24;
        assert rangeMin >= 0 && rangeMax <= 24 && rangeMin < rangeMax;

        temps = temps.subList(rangeMin, rangeMax);

        double cumulativeX = 0;

        List<GraphCell> cells = new ArrayList<>();

        //Draw the cells
        for (int i = 0; i < temps.size(); i++) {
            GraphCell cell = new GraphCell(i);
            content.getChildren().add(cell);
            cell.setLayoutX(cumulativeX);
            cells.add(cell);

            cumulativeX += cell.getWidth();
        }

        int lineGap = dotRadius*3; //Gap between dot centre and line end

        //Draw the lines
        for (int i = 0; i < temps.size() - 1; i++) {
            double dist = Math.sqrt(
                    Math.pow(cellWidth*(i + 0.5) - cellWidth*(i + 1.5), 2) +
                    Math.pow(cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY, 2)
            );

            Line line = new Line(
                    cellWidth*(i + 0.5) + Math.abs(cellWidth*(i + 0.5) - cellWidth*(i + 1.5))*lineGap/dist,
                    cells.get(i).dotCenterY - (cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY)*lineGap/dist,
                    cellWidth*(i + 1.5) - Math.abs(cellWidth*(i + 0.5) - cellWidth*(i + 1.5))*lineGap/dist,
                    cells.get(i + 1).dotCenterY + (cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY)*lineGap/dist
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

    public void deselect() {
        if (selectedCell != null) {
            selectedCell.deselect();
            selectedCell = null;
        }
    }

    class GraphCell extends Pane {

        final int verticalPadding = 10; //Gap between max/min dots and pane top/bottom
        final int hour;
        double temp;
        double dotCenterY;
        Circle dot;
        Label tempLbl;

        public void setColor(Paint paint) {
            setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        public GraphCell(int hour) {
            this.temp = temps.get(hour);
            this.hour = hour;

            double min = Collections.min(temps);
            double max = Collections.max(temps);

            setWidth(cellWidth);
            setHeight(height);
            setPrefSize(cellWidth, height);

            if (hour % 2 == 0) {
                setColor(Color.color(0.72, 0.8, 0.94));
            } else {
                setColor(Color.color(0.57, 0.72, 0.96));
            }

            dot = new Circle(dotRadius, Color.WHITE);
            dotCenterY = dotRadius + verticalPadding + (height - dotRadius*2 - verticalPadding*2)*(1 - (temp - min)/(max - min));
            dot.relocate(
                    cellWidth/2 - dotRadius,
                    dotCenterY - dotRadius
            );

            tempLbl = new Label("" + temps.get(hour).intValue());
            tempLbl.setFont(new Font(20));
            tempLbl.setLayoutY(dotCenterY - 15);
            tempLbl.setPrefWidth(cellWidth);
            tempLbl.setAlignment(Pos.CENTER);

            getChildren().addAll(dot, tempLbl);
            tempLbl.setVisible(false);

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> select()));
        }

        void select() {
            if (selectedCell != null) selectedCell.deselect();
            if (selectedCell == this) {
                deselect();
                Main.getViews().get(ViewName.INITIAL).show();
                return;
            }
            selectedCell = this;

            tempLbl.setVisible(true);

            Main.getViews().get(ViewName.HOURLY).show();
            setColor(Color.WHITE);
            dot.setVisible(false);
        }

        void deselect() {
            tempLbl.setVisible(false);

            if (hour % 2 == 0) {
                setColor(Color.color(0.72, 0.8, 0.94));
            } else {
                setColor(Color.color(0.57, 0.72, 0.96));
            }
            dot.setVisible(true);
        }
    }
}

class clickNotDragHandler implements EventHandler<MouseEvent> {
    boolean dragging = false;
    final EventHandler<MouseEvent> onClickedEventHandler;
    public clickNotDragHandler(EventHandler<MouseEvent> onClickedEventHandler) {
        this.onClickedEventHandler = onClickedEventHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            dragging = false;
        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (!dragging) {
                onClickedEventHandler.handle(event);
            }
        }
    }
}
