package fx;

import backend.Location;
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
import weather.LocationWeatherOWM;
import weather.NoInternetConnection;

import java.util.*;

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

        setStyle("-fx-background-color: transparent;");

        setLayoutY(20);

        setOnDragDetected(e -> dragging = true);

        Pane blankPaneStart = new Pane();
        blankPaneStart.setPrefWidth(Main.screenWidth/2.0 - separation - 25);
        content.getChildren().add(blankPaneStart);

        for (Pane item : items) {
            content.getChildren().add(item);
        }

        Pane blankPaneEnd = new Pane();
        blankPaneEnd.setPrefWidth(Main.screenWidth/2.0 - separation - 25);
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

    public boolean eventSelected() {
        return (selectedItem instanceof EventItem);
    }

    public Location eventLocation() {
        if (selectedItem instanceof EventItem) {
            return ((EventItem) selectedItem).getLocation();
        }
        else {
                return null;
            }
    }

    public int getSelectedDate() {
        return selectedItem.getDate();
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
        double centerX = (boundsInScene.getMaxX() + boundsInScene.getMinX())/2.0;
                //.getCenterX();

        double totalWidth = content.getWidth();
        double newScroll = getHvalue() + (centerX - Main.screenWidth / 2.0) / (totalWidth - Main.screenWidth);

        Animator.timeline(hvalueProperty(), newScroll, 0.2);

        if (selectedItem != item) {
            item.select();
        }
    }

//    public void addEvent(Event event) {
//        EventItem eventItem = new EventItem(event);
//        boolean newDateRequired = true;
//        int insertIndex = 0;
//        for (SelectorItem item : items) {
//            if (item.getDate() == event.getDate()) {
//                newDateRequired = false;
//            }
//            if (item.getDate() <= event.getDate()) {
//                insertIndex++;
//            }
//        }
//
//        if (newDateRequired) {
//            DateItem dateItem = new DateItem(event.getDate());
//            items.add(insertIndex, dateItem);
//            getChildren().add(insertIndex, dateItem);
//            items.add(insertIndex + 1, eventItem);
//            getChildren().add(insertIndex + 1, eventItem);
//        } else {
//            items.add(insertIndex, eventItem);
//            getChildren().add(insertIndex, eventItem);
//        }
//    }

    private List<SelectorItem> makeSomeStuff() {
        List<EventItem> events = new ArrayList<>();
        for (Event event : Main.events) {
            events.add(new EventItem(event));
        }

        List<SelectorItem> items = new ArrayList<>();

        try {
            for (int date : (new LocationWeatherOWM(new Location("Cambridge"))).giveDays()) {
                items.add(new DateItem(date));
            }
        } catch (NoInternetConnection ex) {
            System.err.println("No internet connection. Message: " + ex.getMessage());
        }

        for (EventItem event : events) {
            boolean newDateRequired = true;
            for (SelectorItem item : items) {
                if (item.getDate() == event.getDate()) {
                    newDateRequired = false;
                    break;
                }
            }

            if (newDateRequired) {
                items.add(new DateItem(event.getDate()));
            }
            items.add(event);
        }

        items.sort(Comparator.comparingInt(SelectorItem::getDate));

        return items;
    }

    abstract class SelectorItem extends VBox {
        public SelectorItem(double v) {
            super(v);
        }
        abstract int getDate();
        abstract void select();
        abstract void deselect();
    }

    class DateItem extends SelectorItem {
        Label dateLbl;
        Label monthLbl;
        private int date;

        public DateItem(int date) {
            super(-5);
            this.date = date;
            setWidth(50);
            setPrefWidth(50);
            setPrefHeight(50);
            setAlignment(Pos.CENTER);

            monthLbl = new Label(Event.months[(date % 10000  - date % 100)/100]);
            monthLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 10));

            dateLbl = new Label("" + (date % 100));
            dateLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 30));

            setBackground(new Background(new BackgroundFill(Color.color(1.0, 1.0, 1.0), CornerRadii.EMPTY, Insets.EMPTY)));

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> itemClicked(this)));

            getChildren().addAll(monthLbl, dateLbl);
        }

        @Override
        public int getDate() {
            return date;
        }

        @Override
        void select() {
            if (selectedItem != null) selectedItem.deselect();
            selectedItem = this;

            dateLbl.setTextFill(Color.WHITE);
            monthLbl.setTextFill(Color.WHITE);

            if (Main.temperatureGraph != null) {
                Main.temperatureGraph.deselect();
                Main.temperatureGraph.reloadGraph();
            }

            if (Main.getViews().get(ViewName.INITIAL) != null) {
                Main.getViews().get(ViewName.INITIAL).show();
            }
        }

        @Override
        void deselect() {
            dateLbl.setTextFill(Color.BLACK);
            monthLbl.setTextFill(Color.BLACK);
        }
    }

    class EventItem extends SelectorItem {
        private Event event;

        private Label nameLbl;

        public EventItem(Event event) {
            super(-10);
            this.event = event;
            setPrefHeight(50);
            setAlignment(Pos.CENTER);
            setPadding(new Insets(0,10,0,10));

            nameLbl = new Label(event.getName());
            nameLbl.setFont(Font.loadFont(Main.class.getResourceAsStream("Kollektif.ttf"), 28));

            setBackground(new Background(new BackgroundFill(Color.color(0.35, 0.25, 0.20), CornerRadii.EMPTY, Insets.EMPTY)));

            addEventHandler(MouseEvent.ANY, new clickNotDragHandler(e -> itemClicked(this)));

            getChildren().add(nameLbl);
        }

        public Location getLocation() {
            return event.getLocation();
        }

        @Override
        public int getDate() {
            return event.getDate();
        }

        @Override
        void select() {
            if (selectedItem != null) selectedItem.deselect();
            selectedItem = this;

            nameLbl.setTextFill(Color.WHITE);

            HourlyView hourlyView = (HourlyView) Main.getViews().get(ViewName.HOURLY);
            hourlyView.show();
            hourlyView.showStrategy(event);

            Main.temperatureGraph.deselect();
            Main.temperatureGraph.reloadGraph();
        }

        @Override
        void deselect() {
            nameLbl.setTextFill(Color.BLACK);
        }
    }
}