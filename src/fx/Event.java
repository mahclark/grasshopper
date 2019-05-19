package fx;

import backend.Location;

public class Event {
    final static String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private String name;
    private Location location;
    private String locationName;
    private int date;
    private int startHour;
    private int overs;

    public Event(String name, Location location, int date, int startHour, int overs) {
        this.name = name;
        this.location = location;
        this.locationName = location.getInput();
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

    public String getLocationName() {
        return locationName;
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
