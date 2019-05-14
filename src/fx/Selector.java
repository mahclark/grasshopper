package fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Selector extends ScrollPane {

    private HBox content = new HBox(10);
    private int height = 100;
    final int FADE_THRESHOLD = 100;

    private List<Pane> items = makeSomeStuff();//new ArrayList<>();

    public Selector() {

        for (Pane item : items) {
            content.getChildren().add(item);

            item.addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> selectDate(/*TODO*/)));
        }

        setContent(content);
        setPannable(true);
        setPrefWidth(Main.screenWidth);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Fading
        fadeItems(); //Ensuring objects are faded before scrolling starts.
        hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                fadeItems();
            }
        });
    }

    public void fadeItems() {
        double totalWidth = content.getWidth();
        double scrollCenter = Main.screenWidth/2.0 + (totalWidth - Main.screenWidth)*getHvalue();

        for (Pane item : items) {
            double leftDist = scrollCenter - item.getLayoutX();
            double rightDist = scrollCenter - (item.getLayoutX() + item.getWidth());

            double maxScreenEdgeDist = Main.screenWidth/2.0 - Math.min(
                    Math.abs(leftDist),
                    Math.abs(rightDist)
            );

            if (maxScreenEdgeDist >= FADE_THRESHOLD || leftDist/Math.abs(leftDist) != rightDist/Math.abs(rightDist) || totalWidth == 0.0) {
                item.setOpacity(1);
            } else {
                item.setOpacity(maxScreenEdgeDist/FADE_THRESHOLD);
            }
        }
    }

    public List<Pane> makeSomeStuff() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Match vs Oxford", 1, 5));
        events.add(new Event("M2", 4, 5));
        events.add(new Event("Rematch vs Oxford", 10, 5));
        events.add(new Event("Training", 10, 5));

        List<Pane> items = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            items.add(new Date(i, 5));

            if (!events.isEmpty() && events.get(0).date == i) {
                items.add(events.remove(0));
            }
            if (!events.isEmpty() && events.get(0).date == i) {
                items.add(events.remove(0));
            }
        }
        return items;
    }

    private void selectDate() {
        System.out.println("A date has been selected");
        Main.getViews().get(ViewName.INITIAL).show();

        Main.temperatureGraph.selectedCell.deselect();
    }
}

class Date extends VBox {
    final String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public Date(int date, int month) {
        super(-10);
        setWidth(50);
        setPrefWidth(50);
        setPrefHeight(50);
        setAlignment(Pos.CENTER);

        Label monthLbl = new Label(months[month]);
        monthLbl.setFont(new Font(10));

        Label dateLbl = new Label("" + date);
        dateLbl.setFont(new Font(30));

        setBackground(new Background(new BackgroundFill(Color.color(0.57, 0.72, 0.96), CornerRadii.EMPTY, Insets.EMPTY)));

        getChildren().addAll(monthLbl, dateLbl);
    }
}

class Event extends VBox {
    int month;
    int date;

    public Event(String name, int date, int month) {
        super(-10);
        this.date = date;
        setPrefHeight(50);
        setAlignment(Pos.CENTER);

        Label nameLbl = new Label(name);
        nameLbl.setFont(new Font(28));

        setBackground(new Background(new BackgroundFill(Color.color(0.94, 0.89, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

        getChildren().add(nameLbl);
    }
}