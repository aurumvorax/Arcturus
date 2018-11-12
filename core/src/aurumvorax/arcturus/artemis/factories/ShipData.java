package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class ShipData{

    public String imgName;
    public Vector2 imgCenter;
    int collisionRadius;
    Array<Array<Vector2>> vertices;
    public Array<Mount.Weapon> weaponMounts;
    float hull;
    HashMap<String, Loadout> loadouts;

    //TODO Move this to PlayerData
    Map<String, ShipProfile> currentProfiles = new HashMap<>();     //Indexed by ship name


    //TODO Move this to world populator
    public static int buildGeneric(String name, String type, String loadout, float x, float y, float t){
        return ShipFactory.create(new ShipProfile(name, type, loadout), x, y, t);
    }
}