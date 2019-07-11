package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.Mount;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;

public class ShipData{

    public static class Stock{
        public String imgName;
        public Vector2 imgCenter;
        int collisionRadius;
        Array<Array<Vector2>> vertices;
        public Array<Mount.Weapon> weaponMounts;
        public float hull;
        public HashMap<String, Loadout> loadouts;
    }

    public static class Unique{
        public String name;
        public String type;
        public float x, y, t;
        public Loadout loadout;
        public float health;

        public Unique(){}

        public Unique(String name, String type){
            this.name = name;
            this.type = type;
            loadout = new Loadout();
        }

        public Unique(Unique ship){
            this.name = ship.name;
            this.type = ship.type;
            this.x = ship.x;
            this.y = ship.y;
            this.t = ship.t;
            this.loadout = new Loadout(ship.loadout);
            this.health = ship.health;
        }
    }

    public static class Loadout{
        public IntMap<String> weapons = new IntMap<>();

        public Loadout(){}

        Loadout(Loadout l){
            for(int i = 0; i < l.weapons.size; i++){
                this.weapons.put(i, l.weapons.get(i));
            }
        }
    }

    public static boolean verifyStock(ShipData.Stock data){
        if(data == null)
            return false;

        return(data.imgName != null &&
            data.imgCenter != null &&
            data.collisionRadius > 0 &&
            data.vertices != null &&
            data.weaponMounts != null &&
            data.hull > 0);
    }

    public static boolean verifyUnique(ShipData.Unique data){
        if(data == null)
            return false;

        return(data.name != null &&
            data.type != null &&
            data.health > 0 &&
            data.loadout != null);
    }

    public static boolean verifyLoadout(ShipData.Loadout data){
        if(data == null)
            return false;

        return(data.weapons != null);
    }
}