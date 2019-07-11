package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class Arrive{
    private static Arrive INSTANCE = new Arrive();
    private Arrive(){} // Single static class with DI/callback

    private static Vector2 arrive = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;


    public static void initialize(World world){ world.inject(INSTANCE); }

    public static Vector2 calc(int owner, int target, float margin){
        if(!mPhysics.has(target))
            return null;
        Physics2D ownerP = mPhysics.get(owner);
        Physics2D targetP = mPhysics.get(target);
        float distance = ownerP.p.dst(targetP.p);

        if(distance <= margin)  // Already there
            return Stop.calc(owner);

        PoweredMotion pm = mPowered.get(owner);
        float deccel = ownerP.v.len2() * 0.5f /  pm.thrust;

        if(distance - margin > deccel)   // Not close enough to slow down yet
            return arrive.set(targetP.p).sub(ownerP.p).setLength(pm.thrust);
        else
            return arrive.set(ownerP.v).scl(-1f).setLength(pm.thrust);
    }
}
