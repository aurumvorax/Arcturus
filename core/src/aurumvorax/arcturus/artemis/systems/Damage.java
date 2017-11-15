package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Health;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Damage extends BaseSystem{

    private static IntBag destroyed = new IntBag();

    private static ComponentMapper<Health> mHealth;
    private static ComponentMapper<Weapons> mWeapons;

    public static void hull(int entityID, Vector2 location, float damage){
        if(mHealth.has(entityID)){
            Health h = mHealth.get(entityID);
            h.hull -= damage;
            if(h.hull <= 0){
                destroyEntity(entityID);
            }
        }
    }

    public static void destroyEntity(int entityID){
        destroyed.add(entityID);
        if(mWeapons.has(entityID))
            for(int w : mWeapons.get(entityID).all.toArray())
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
