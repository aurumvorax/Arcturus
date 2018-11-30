package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
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


}