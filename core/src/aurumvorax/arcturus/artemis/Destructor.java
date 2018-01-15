package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class Destructor extends BaseSystem{

    private static IntBag destroyed = new IntBag();

    private static ComponentMapper<Weapons> mWeapons;

    public static void safeRemove(int entityID){
        destroyed.add(entityID);
        if(mWeapons.has(entityID))
            for(int w : mWeapons.get(entityID).all.getData())
                destroyed.add(w);

    }

    @Override
    protected void processSystem(){
        for(int i = 0; i < destroyed.size(); i++){
            world.delete(destroyed.get(i));
        }
        destroyed.clear();
    }
}
