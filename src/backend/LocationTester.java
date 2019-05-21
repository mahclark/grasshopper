package backend;

import java.io.IOException;

public class LocationTester {

    public static void main (String[] arg) throws IOException {
        Location testLoc = new Location("Cambridge University Computer Lab");
        System.out.println("Lat: " + testLoc.getLat());
        System.out.println("Lon: " + testLoc.getLon());
        System.out.println("PlaceId:" + testLoc.getPlaceId());

        System.out.println("What was found for 'Cam':");

        for (String s : Location.getLocation("Chic", true)) {
            System.out.println(s);
        }
        Location chick = new Location("Chicken");
        System.out.println(chick.getLat());


    }

}
