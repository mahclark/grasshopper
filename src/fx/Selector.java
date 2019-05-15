package fx;

import javafx.animation.FadeTransition;
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
    private SelectorItem selectedItem;

    private int height = 100;
    final int FADE_THRESHOLD = 100;

    private List<SelectorItem> items = makeSomeStuff();//new ArrayList<>();

    public Selector() {

        for (Pane item : items) {
            content.getChildren().add(item);
        }

        setContent(content);
        setPannable(true);
        setPrefWidth(Main.screenWidth);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Fading
//        fadeItems(); //Ensuring objects are faded before scrolling starts.
        hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                fadeItems();
            }
        });
    }

    public void fadeItems() {
        double totalWidth = content.getWidth();
        double scrollCenter = Main.screenWidth / 2.0 + (totalWidth - Main.screenWidth) * getHvalue();

        for (SelectorItem item : items) {
            double leftDist = scrollCenter - item.getLayoutX();
            double rightDist = scrollCenter - (item.getLayoutX() + item.getWidth());

            double maxScreenEdgeDist = Main.screenWidth / 2.0 - Math.min(
                    Math.abs(leftDist),
                    Math.abs(rightDist)
            );

            if (maxScreenEdgeDist >= FADE_THRESHOLD || leftDist / Math.abs(leftDist) != rightDist / Math.abs(rightDist)) {
                item.setOpacity(1);
            } else {
                item.setOpacity(maxScreenEdgeDist / FADE_THRESHOLD);
            }
        }
    }

    public List<SelectorItem> makeSomeStuff() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Match vs Oxford", 1, 5));
        events.add(new Event("M2", 4, 5));
        events.add(new Event("Rematch vs Oxford", 10, 5));
        events.add(new Event("Training", 10, 5));

        List<SelectorItem> items = new ArrayList<>();
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

        Main.temperatureGraph.deselect();
    }

    abstract class SelectorItem extends VBox {
        public FadeTransition fadeTransition;
        public SelectorItem(double v) {
            super(v);
        }
        abstract void select();
        abstract void deselect();
    }

    class Date extends SelectorItem {
        final String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        Label dateLbl;
        Label monthLbl;

        public Date(int date, int month) {
            super(-10);
            setWidth(50);
            setPrefWidth(50);
            setPrefHeight(50);
            setAlignment(Pos.CENTER);

            monthLbl = new Label(months[month]);
            monthLbl.setFont(new Font(10));

            dateLbl = new Label("" + date);
            dateLbl.setFont(new Font(30));

            setBackground(new Background(new BackgroundFill(Color.color(0.57, 0.72, 0.96), CornerRadii.EMPTY, Insets.EMPTY)));

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> select()));

            getChildren().addAll(monthLbl, dateLbl);
        }

        @Override
        void select() {
            if (selectedItem != null) selectedItem.deselect();
            selectedItem = this;

            dateLbl.setTextFill(Color.WHITE);
            monthLbl.setTextFill(Color.WHITE);
            Main.getViews().get(ViewName.INITIAL).show();

            Main.temperatureGraph.deselect();

            /*TODO: make call in HourlyView*/
        }

        @Override
        void deselect() {
            dateLbl.setTextFill(Color.BLACK);
            monthLbl.setTextFill(Color.BLACK);
        }
    }

    class Event extends SelectorItem {
        int month;
        int date;

        Label nameLbl;

        public Event(String name, int date, int month) {
            super(-10);
            this.date = date;
            setPrefHeight(50);
            setAlignment(Pos.CENTER);
            setPadding(new Insets(0,10,0,10));

            nameLbl = new Label(name);
            nameLbl.setFont(new Font(28));

            setBackground(new Background(new BackgroundFill(Color.color(0.94, 0.89, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> select()));

            getChildren().add(nameLbl);
        }

        @Override
        void select() {
            if (selectedItem != null) selectedItem.deselect();
            selectedItem = this;

            nameLbl.setTextFill(Color.WHITE);
            Main.getViews().get(ViewName.HOURLY).show();

            Main.temperatureGraph.deselect();

            /*TODO: make call in HourlyView*/
        }

        @Override
        void deselect() {
            nameLbl.setTextFill(Color.BLACK);
        }
    }
}