package aurumvorax.arcturus;


import aurumvorax.arcturus.artemis.factories.ShipProfile;


public class PlayerData{

    public static long money = 100;
    private static ShipProfile playership;
    public static float x, y, t;  // will be replaced with name of station, once such things are made
    public static String navTarget;

    public static ShipProfile GetPlayerShip(){ return new ShipProfile(playership); }
    public static void SetPlayerShip(ShipProfile newShip){ playership = newShip; }

}
