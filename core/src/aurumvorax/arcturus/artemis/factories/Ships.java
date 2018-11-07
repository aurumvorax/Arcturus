package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;

public class Ships{

    //TODO Move this to PlayerData
    Map<String, Profile> currentProfiles = new HashMap<>();     //Indexed by ship name


    //TODO Move this to world populator
    public static int buildGeneric(String name, String type, String loadout, float x, float y, float t){
        return ShipFactory.create(new Profile(name, type, loadout), x, y, t);
    }

    public static class Data{

        public String imgName;
        public Vector2 imgCenter;
        int collisionRadius;
        Array<Array<Vector2>> vertices;
        public Array<Mount.Weapon> weaponMounts;
        float hull;
        HashMap<String, Loadout> loadouts;
    }

    public static class Loadout{
        public IntMap<String> weapons = new IntMap<>();
    }

    public static class Profile{
        public Profile(String n, String t, String l){
            name = n;
            type = t;
            if(l == null)
                loadout = new Loadout();
            else
                loadout = EntityData.getShipData(type).loadouts.get(l);
        }

        public String name;
        public String type;

        public Loadout loadout;
    }

}