package aurumvorax.arcturus.backstage;

import aurumvorax.arcturus.artemis.factories.ShipData;
import aurumvorax.arcturus.services.EntityData;
import com.badlogic.gdx.utils.IntMap;

public class Profiles{

    public static class Solar{

    }

    public static class Ship{
        public String name;
        public String type;
        public Loadout loadout;
        public float health;

        public Ship(String n, String t, String l){
            name = n;
            type = t;
            ShipData data = EntityData.getShipData(type);
            loadout = (l == null) ? new Loadout() : data.loadouts.get(l);
            health = data.hull;
        }

        public Ship(Ship s){
            name = s.name;
            type = s.type;
            loadout = new Loadout(s.loadout);
            health = s.health;
        }

        public static class Loadout{
            public IntMap<String> weapons = new IntMap<>();

            public Loadout(){}

            public Loadout(String type, String preset){
                this(EntityData.getShipData(type).loadouts.get(preset));
            }

            public Loadout(Loadout l){
                for(int w = 0; w < l.weapons.size; w++)
                    weapons.put(w, l.weapons.get(w));
            }
        }
    }
}
