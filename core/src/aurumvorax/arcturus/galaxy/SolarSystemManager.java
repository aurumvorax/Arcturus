package aurumvorax.arcturus.galaxy;

import aurumvorax.arcturus.artemis.factories.OrbitalData;
import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.artemis.factories.StellarFactory;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.screens.GameScreen;
import aurumvorax.arcturus.services.EntityData;

import java.util.HashMap;

public class SolarSystemManager{

    private static String currentSystem;
    private static String navigationTarget = null;
    private static HashMap<String, StellarData.Extra> solarSystems = new HashMap<>();


    public static void setNavigationTarget(String target){ navigationTarget = target; }
    public static String getCurrentSystem(){ return currentSystem; }

    public static void loadSystem(String systemName){
        StellarData.Base base = EntityData.getStellarData(systemName);
        currentSystem = systemName;

        int starID = StellarFactory.createStar(base.starName, base.starImgName, base.starImgCenter);
        for(OrbitalData child : base.children){
            StellarFactory.buildTree(child, starID);
        }

        StellarData.Extra extra = solarSystems.get(systemName);
        if(extra == null)
            return;

        for(ShipData.Unique ship : extra.ships.items)
            ShipFactory.create(ship);
    }

    public static void saveSystem(){
        StellarData.Extra save = StellarFactory.extractCurrentSystem();
        solarSystems.put(currentSystem, save);
    }

    public static void hyperdrive(){
        if((navigationTarget == null) || (navigationTarget.equals(currentSystem)))
            return;

        PlayerShip.extract();
        StellarData.Extra save = StellarFactory.extractCurrentSystem();
        solarSystems.put(currentSystem, save);

        GameScreen.resetWorld();

        PlayerShip.insert();
        loadSystem(navigationTarget);
    }
}
