package aurumvorax.arcturus;


import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.factories.ShipData;

public class PlayerData{

    public static long money = 1000;
    private static ShipData.Unique playerShip;
    public static Physics2D state = new Physics2D();

    public static ShipData.Unique GetPlayerShip(){ return new ShipData.Unique(playerShip); }
    public static void SetPlayerShip(ShipData.Unique newShip){ playerShip = newShip; }

}
