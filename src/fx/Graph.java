package fx;

import backend.Location;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import weather.LocationWeatherOWM;
import weather.NoInternetConnection;

import java.util.*;

public class Graph extends ScrollPane {

    private Pane content = new Pane();

    private int graphHeight = 200;
    private double cellWidth = Main.screenWidth/8.0;
    private final int dotRadius = (int) cellWidth/7;

    private int rangeMin = 0;
    private int rangeMax = 24; //Index of the first value which won't be shown

    private GraphCell selectedCell; //null when nothing selected

    private TreeMap<Integer, Float> temperatures = new TreeMap<>();

    private String emptyErrorMessage = "No data available.";

    public Graph() {
        setStyle("-fx-focus-color: transparent;");
        System.err.println("Cambridge location:\t" + Main.getUserLocation().getLon() + "\t" + Main.getUserLocation().getLat());

        reloadGraph();

        setContent(content);
        setPannable(true);
        setPrefWidth(Main.screenWidth);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void reloadGraph() {
        reloadData();
        reloadVisuals();
    }

    private void reloadData() {
        temperatures = new TreeMap<>();

        try {
            LocationWeatherOWM weatherOWM;
            int date = Main.selector.getSelectedDate();
            List<Integer> hours;
            if (Main.selector.eventSelected()) {
                weatherOWM = new LocationWeatherOWM(Main.selector.eventLocation());
                hours = ((HourlyView) Main.getViews().get(ViewName.HOURLY)).getRelevantWeatherHours();
            } else {
                weatherOWM = new LocationWeatherOWM(Main.getUserLocation());
                hours = weatherOWM.giveHours(date);
            }

            if (hours != null) {
                for (int hour : hours) {
                    if (weatherOWM.giveHours(date).contains(hour)) {
                        temperatures.put(hour, weatherOWM.giveData(date, hour).getTemp());
                    }
                }
            }

            emptyErrorMessage = "No data available.";
        } catch (NoInternetConnection e) {
            System.err.println("No internet connection.");
            emptyErrorMessage = "No internet connection detected.";
        }
    }

    private void reloadVisuals() {
        getChildren().clear();
        if (temperatures.size() == 0 || (temperatures.size() == 1 && Main.selector.eventSelected())) {
            content = new Pane();
            content.setPrefWidth(Main.screenWidth);
            content.setPrefHeight(graphHeight);

            Label errorLbl = new Label(emptyErrorMessage);
            errorLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 20));
            errorLbl.setPrefWidth(Main.screenWidth);
            errorLbl.setPrefHeight(graphHeight);
            errorLbl.setWrapText(true);
            errorLbl.setAlignment(Pos.CENTER);
            errorLbl.setContentDisplay(ContentDisplay.CENTER);

            content.getChildren().add(errorLbl);
            getChildren().add(content);
        } else {
            content = new Pane();
            getChildren().add(content);

            cellWidth = Main.screenWidth/temperatures.size();

            double cumulativeX = 0;

            List<GraphCell> cells = new ArrayList<>();

            //Draw the cells
            for (int hour : temperatures.keySet()) {
                GraphCell cell = new GraphCell(hour);
                content.getChildren().add(cell);
                cell.setLayoutX(cumulativeX);
                cells.add(cell);

                cumulativeX += cell.getWidth();
            }

            int lineGap = dotRadius * 3; //Gap between dot centre and line end

            //Draw the lines
            for (int i = 0; i < temperatures.size() - 1; i++) {
                double dist = Math.sqrt(
                        Math.pow(cellWidth * (i + 0.5) - cellWidth * (i + 1.5), 2) +
                                Math.pow(cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY, 2)
                );

                Line line = new Line(
                        cellWidth * (i + 0.5) + Math.abs(cellWidth * (i + 0.5) - cellWidth * (i + 1.5)) * lineGap / dist,
                        cells.get(i).dotCenterY - (cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY) * lineGap / dist,
                        cellWidth * (i + 1.5) - Math.abs(cellWidth * (i + 0.5) - cellWidth * (i + 1.5)) * lineGap / dist,
                        cells.get(i + 1).dotCenterY + (cells.get(i).dotCenterY - cells.get(i + 1).dotCenterY) * lineGap / dist
                );
                line.setStroke(Color.WHITE);
                line.setStrokeWidth(dotRadius / 2.0);
                content.getChildren().add(line);
            }
        }
        Animator.fade(content, 0.0, 1.0, 0.3);
    }

    public void deselect() {
        if (selectedCell != null) {
            selectedCell.deselect();
            selectedCell = null;
        }
    }

    public String getAvgTemperature() {
        if (temperatures.size() == 0) return "-";

        int count = 0;
        double total = 0;
        for (int i = 9; i <= 18; i += 3) {
            if (temperatures.containsKey(i)) {
                count += 1;
                total += temperatures.get(i);
            }
        }
        if (count == 0) {
            for (double temp : temperatures.values()) {
                count += 1;
                total += temp;
            }
        }

        return "" + (int) total/count;
    }

    class GraphCell extends Pane {

        final int verticalPadding = 10; //Gap between max/min dots and pane top/bottom
        final int hour;
        double temp;
        double dotCenterY;
        Circle dot;
        Label tempLbl;
        boolean empty;

        public void setColor(Paint paint) {
            setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        public GraphCell(int hour) {
            this.hour = hour;

            if (hour % 2 == 0) {
                setColor(Color.color(0.72, 0.8, 0.94));
            } else {
                setColor(Color.color(0.57, 0.72, 0.96));
            }

            setWidth(cellWidth);
            setHeight(graphHeight);
            setPrefSize(cellWidth, graphHeight);

            int timeHeight = 20;

            empty = (!temperatures.containsKey(hour));
            if (!empty) {
                this.temp = temperatures.get(hour);

                double min = Collections.min(temperatures.values());
                double max = Collections.max(temperatures.values());

                if (min == max) {
                    min -= 1;
                    max += 1;
                }

                dot = new Circle(dotRadius, Color.WHITE);
                dotCenterY = dotRadius + verticalPadding + (graphHeight - timeHeight - dotRadius * 2 - verticalPadding * 2) * (1 - (temp - min) / (max - min));
                dot.relocate(
                        cellWidth / 2 - dotRadius,
                        dotCenterY - dotRadius
                );

                Label timeLbl = new Label("" + hour);
                timeLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), timeHeight - 3));
                timeLbl.setAlignment(Pos.CENTER);
                timeLbl.setPrefWidth(cellWidth);

                Pane lblPane = new Pane();
                lblPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                lblPane.getChildren().add(timeLbl);
                lblPane.setLayoutY(graphHeight - timeHeight + 2);
                lblPane.setPrefWidth(cellWidth);
                lblPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");

                tempLbl = new Label();
                if (Main.gettempformat()){
                    tempLbl.setText(Math.round(temperatures.get(hour).intValue()*9.0/5) + 32 + "°");
                } else {
                    tempLbl.setText(temperatures.get(hour).intValue() + "°");
                }
                tempLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 24));
                tempLbl.setLayoutY(dotCenterY - 15);
                tempLbl.setPrefWidth(cellWidth);
                tempLbl.setAlignment(Pos.CENTER);
                tempLbl.setTextFill(Color.WHITE);

                getChildren().addAll(dot, tempLbl, lblPane);
                dot.setVisible(false);

                addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> select()));
            }
        }

        void select() {
            if (!Main.selector.eventSelected() && !empty) {
                if (selectedCell != null) selectedCell.deselect();
                if (selectedCell == this) {
                    selectedCell = null;
                    Main.getViews().get(ViewName.INITIAL).show();
                    return;
                }
                selectedCell = this;

                tempLbl.setVisible(true);
                if (Main.gettempformat()){
                    tempLbl.setText(Math.round(temperatures.get(hour).intValue()*9.0/5) + 32 + "°");
                } else {
                    tempLbl.setText(temperatures.get(hour).intValue() + "°");
                }

                HourlyView hourlyView = (HourlyView) Main.getViews().get(ViewName.HOURLY);
                hourlyView.show();
                hourlyView.showHourlyWeather(hour);

                setColor(Color.WHITE);
                tempLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 30));
                tempLbl.setTextFill(Color.BLACK);
            }
        }

        void deselect() {
            if (hour % 2 == 0) {
                setColor(Color.color(0.72, 0.8, 0.94));
            } else {
                setColor(Color.color(0.57, 0.72, 0.96));
            }
            tempLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 24));
            tempLbl.setTextFill(Color.WHITE);
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
