package fx;

import backend.Location;

public class Event {
    private String name;
    private Location location;
    private int date;
    private int startHour;
    private int overs;

    public Event(String name, Location location, int date, int startHour, int overs) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.overs = overs;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getDate() {
        return date;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getOvers() {
        return overs;
    }
}
