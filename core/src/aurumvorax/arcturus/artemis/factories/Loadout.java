package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.utils.IntMap;

public class Loadout{

        public IntMap<String> weapons = new IntMap<>();

        public Loadout(){}

        public Loadout(String type, String preset){
            this(EntityData.getShipData(type).loadouts.get(preset));
        }

        public Loadout(Loadout l){
            for(int w = 0; w < weapons.size; w++)
                weapons.put(w, l.weapons.get(w));
        }

}
