package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.screens.MenuScreen;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class Destructor extends BaseSystem{

    private static IntBag destroyed = new IntBag();

    private static ComponentMapper<Weapons> mWeapons;

    public static void safeRemove(int entityID){
        destroyed.add(entityID);
        if(mWeapons.has(entityID)){
            IntBag weapons = mWeapons.get(entityID).all;
            for(int i = 0; i < weapons.size(); i++)
                destroyed.add(weapons.get(i));
        }
    }

    @Override
    protected void processSystem(){
        for(int i = 0; i < destroyed.size(); i++){
            world.delete(destroyed.get(i));
        }
        destroyed.clear();
    }
}
