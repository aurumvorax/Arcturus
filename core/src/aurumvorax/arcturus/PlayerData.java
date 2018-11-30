package aurumvorax.arcturus;


import aurumvorax.arcturus.artemis.factories.ShipData;

public class PlayerData{

    public static long money = 100;
    private static ShipData.Unique playership;
    public static float x, y, t;  // will be replaced with name of station, once such things are made
    public static String navTarget;

    public static ShipData.Unique GetPlayerShip(){ return new ShipData.Unique(playership); }
    public static void SetPlayerShip(ShipData.Unique newShip){ playership = newShip; }

}
