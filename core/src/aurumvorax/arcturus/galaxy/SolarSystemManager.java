package aurumvorax.arcturus.galaxy;

import aurumvorax.arcturus.artemis.factories.OrbitalData;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.artemis.factories.StellarFactory;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.savegame.SaveObserver;
import aurumvorax.arcturus.screens.GameScreen;
import aurumvorax.arcturus.services.EntityData;

import java.util.HashMap;

public class SolarSystemManager implements SaveObserver{

    private static String currentSystem;
    private static String navigationTarget = null;
    private static HashMap<String, StellarData.Extra> solarSystems = new HashMap<>();


    public static void setNavigationTarget(String target){ navigationTarget = target; }
    public static String getCurrentSystem(){ return currentSystem; }
    public static void resetWorlds(){ solarSystems.clear(); }

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

        for(int i = 0; i < extra.ships.size; i++)
            ShipFactory.create(extra.ships.get(i));
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

    @SuppressWarnings("unchecked")
    @Override
    public void onNotify(SaveManager saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case SAVING:
                saveManager.saveElement("CurrentSystem", currentSystem);
                saveManager.saveElement("NavigationTarget", navigationTarget);
                saveManager.saveElement("SolarSystemList", solarSystems);
                break;

            case LOADING:
                currentSystem = saveManager.loadElement("CurrentSystem", String.class);
                navigationTarget = saveManager.loadElement("NavigationTarget", String.class);
                solarSystems = saveManager.loadElement("SolarSystemList", HashMap.class);
                break;
        }
    }
}
