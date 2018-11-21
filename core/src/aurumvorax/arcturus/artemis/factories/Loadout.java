package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.services.EntityData;
import com.badlogic.gdx.utils.IntMap;

public class Loadout{

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
