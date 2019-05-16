package fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Selector extends ScrollPane {

    private int height = 100;
    final int FADE_THRESHOLD = 100;
    final int SCALE_THRESHOLD = 70;
    final int separation = 5;

    private HBox content = new HBox(separation);
    private SelectorItem selectedItem;
    private boolean dragging = false;


    private List<SelectorItem> items = makeSomeStuff();//new ArrayList<>();

    public Selector() {
        setStyle("-fx-focus-color: transparent;");

        setOnDragDetected(e -> dragging = true);

        Pane blankPaneStart = new Pane();
        blankPaneStart.setPrefWidth(Main.screenWidth/2 - separation - 25);
        content.getChildren().add(blankPaneStart);

        for (Pane item : items) {
            content.getChildren().add(item);
        }

        Pane blankPaneEnd = new Pane();
        blankPaneEnd.setPrefWidth(Main.screenWidth/2 - separation - 25);
        content.getChildren().add(blankPaneEnd);

        items.get(0).select();

        setContent(content);
        setPannable(true);
        setPrefWidth(Main.screenWidth);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Fading
        hvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                reformatItems();
            }
        });
    }

    public void reformatItems() {
        double totalWidth = content.getWidth();
        double scrollCenter = Main.screenWidth / 2.0 + (totalWidth - Main.screenWidth) * getHvalue();

        double minFromCenter = Double.POSITIVE_INFINITY;
        SelectorItem middleItem = null;

        for (SelectorItem item : items) {
            double leftDist = scrollCenter - item.getLayoutX();
            double rightDist = scrollCenter - (item.getLayoutX() + item.getWidth());

            double maxScreenEdgeDist = Main.screenWidth / 2.0 - Math.min(
                    Math.abs(leftDist),
                    Math.abs(rightDist)
            );

            double minScreenMiddleDist = Main.screenWidth / 2 - maxScreenEdgeDist;

            if (Math.abs(minScreenMiddleDist) < minFromCenter) {
                minFromCenter = Math.abs(minScreenMiddleDist);
                middleItem = item;
            }

            if (maxScreenEdgeDist >= SCALE_THRESHOLD || leftDist / Math.abs(leftDist) != rightDist / Math.abs(rightDist)) {
                item.setScaleX(0.9);
                item.setScaleY(0.9);
            } else {
                item.setScaleX(0.5 + 0.4 * maxScreenEdgeDist / SCALE_THRESHOLD);
                item.setScaleY(0.3 + 0.6 * maxScreenEdgeDist / SCALE_THRESHOLD);
            }

            if (maxScreenEdgeDist >= FADE_THRESHOLD || leftDist / Math.abs(leftDist) != rightDist / Math.abs(rightDist)) {
                item.setOpacity(1);
            } else {
                item.setOpacity(maxScreenEdgeDist / FADE_THRESHOLD);
            }
        }

        if (middleItem != null) {
            middleItem.setScaleX(1);
            middleItem.setScaleY(1);
        }
    }

    public void mouseUp() {
        if (dragging) {
            double totalWidth = content.getWidth();
            double scrollCenter = Main.screenWidth / 2.0 + (totalWidth - Main.screenWidth) * getHvalue();
            double minFromCenter = Double.POSITIVE_INFINITY;
            SelectorItem middleItem = null;

            for (SelectorItem item : items) {
                double leftDist = scrollCenter - item.getLayoutX();
                double rightDist = scrollCenter - (item.getLayoutX() + item.getWidth());

                double maxScreenEdgeDist = Main.screenWidth / 2.0 - Math.min(
                        Math.abs(leftDist),
                        Math.abs(rightDist)
                );

                double minScreenMiddleDist = Main.screenWidth / 2.0 - maxScreenEdgeDist;

                if (Math.abs(minScreenMiddleDist) < minFromCenter) {
                    minFromCenter = Math.abs(minScreenMiddleDist);
                    middleItem = item;
                }
            }

            if (middleItem != null && middleItem != selectedItem) {
                middleItem.select();
                reformatItems();

                middleItem.setScaleX(1);
                middleItem.setScaleY(1);
            }

            itemClicked(middleItem);
        }
        dragging = false;
    }

    private void itemClicked(SelectorItem item) {
        Bounds boundsInScene = item.localToScene(item.getBoundsInLocal());
        double centerX = boundsInScene.getCenterX();

        double totalWidth = content.getWidth();
        double newScroll = (totalWidth * getHvalue() + 1.16*(centerX - Main.screenWidth / 2.0)) / totalWidth;

        Animator.timeline(hvalueProperty(), newScroll, 0.2);

        item.select();
    }

    private List<SelectorItem> makeSomeStuff() {
        List<EventItem> events = new ArrayList<>();
        events.add(new EventItem("Match vs Oxford", 1, 5));
        events.add(new EventItem("M2", 4, 5));
        events.add(new EventItem("Rematch vs Oxford", 10, 5));
        events.add(new EventItem("Training", 10, 5));

        List<SelectorItem> items = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            items.add(new DateItem(i, 5));

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
        public SelectorItem(double v) {
            super(v);
        }
        abstract void select();
        abstract void deselect();
    }

    class DateItem extends SelectorItem {
        final String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        Label dateLbl;
        Label monthLbl;

        public DateItem(int date, int month) {
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

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> itemClicked(this)));

            getChildren().addAll(monthLbl, dateLbl);
        }

        @Override
        void select() {
            if (selectedItem != null) selectedItem.deselect();
            selectedItem = this;

            dateLbl.setTextFill(Color.WHITE);
            monthLbl.setTextFill(Color.WHITE);
            if (Main.getViews().get(ViewName.INITIAL) != null) {
                Main.getViews().get(ViewName.INITIAL).show();
            }

            Main.temperatureGraph.deselect();

            /*TODO: make call in HourlyView*/
        }

        @Override
        void deselect() {
            dateLbl.setTextFill(Color.BLACK);
            monthLbl.setTextFill(Color.BLACK);
        }
    }

    class EventItem extends SelectorItem {
        int month;
        int date;

        Label nameLbl;

        public EventItem(String name, int date, int month) {
            super(-10);
            this.date = date;
            setPrefHeight(50);
            setAlignment(Pos.CENTER);
            setPadding(new Insets(0,10,0,10));

            nameLbl = new Label(name);
            nameLbl.setFont(new Font(28));

            setBackground(new Background(new BackgroundFill(Color.color(0.94, 0.89, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> itemClicked(this)));

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