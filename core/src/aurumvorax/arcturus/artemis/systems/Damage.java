package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.artemis.EffectFactory;
import aurumvorax.arcturus.artemis.components.Health;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Damage extends BaseSystem{

    private static ComponentMapper<Health> mHealth;
    private static ComponentMapper<Physics2D> mPhysics;

    public static void hull(int entityID, Vector2 location, float damage){
        if(mHealth.has(entityID)){
            Health h = mHealth.get(entityID);
            h.hull -= damage;
            if(h.hull <= 0){
                EffectFactory.createExplosion("", mPhysics.get(entityID));
                Destructor.safeRemove(entityID);
            }
        }
    }

    @Override
    protected void processSystem(){}
}
