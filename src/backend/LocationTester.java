package backend;

public class LocationTester {

    public static void main (String[] arg){
        Location testLoc = new Location("Cambridge University Computer Lab");
        System.out.println(testLoc.getLat());
        System.out.println(testLoc.getLon());
        System.out.println(testLoc.getPlaceId());
    }

}
