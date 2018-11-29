package aurumvorax.arcturus;


import aurumvorax.arcturus.backstage.Profiles;


public class PlayerData{

    public static long money = 100;
    private static Profiles.Ship playership;
    public static float x, y, t;  // will be replaced with name of station, once such things are made
    public static String navTarget;

    public static Profiles.Ship GetPlayerShip(){ return new Profiles.Ship(playership); }
    public static void SetPlayerShip(Profiles.Ship newShip){ playership = newShip; }

}
