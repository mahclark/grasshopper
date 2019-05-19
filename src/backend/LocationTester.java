package backend;

import java.io.IOException;

public class LocationTester {

    public static void main (String[] arg) throws IOException {
        Location testLoc = new Location("Cambridge University Computer Lab");
        System.out.println(testLoc.getLat());
        System.out.println(testLoc.getLon());
        System.out.println(testLoc.getPlaceId());

        for (String s : Location.getLocation("Cam", true)) {
            System.out.println(s);
        }
    }

}
