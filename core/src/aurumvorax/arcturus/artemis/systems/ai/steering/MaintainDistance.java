package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class MaintainDistance{
    private static MaintainDistance INSTANCE = new MaintainDistance();
    private MaintainDistance(){} // Single static class with DI/callback

    private static final float MARGIN = 10f;
    private static Vector2 destination = new Vector2();
    private static Vector2 maintain = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;

    public static void initialize(World world){ world.inject(INSTANCE); }

    public static Vector2 calc(int owner, int target, float distance){
        if(!mPhysics.has(target))
            return null;

        Physics2D ownerP = mPhysics.get(owner);
        Physics2D targetP = mPhysics.get(target);


        destination.set(0,distance);
        destination.setAngle(targetP.p.angle(ownerP.p)).add(targetP.p);
        float difference = ownerP.p.dst(destination);

        if(difference <= MARGIN)
            return Stop.calc(owner);

        PoweredMotion pm = mPowered.get(owner);
        float deccel = ownerP.v.len2() * 0.5f /  pm.thrust;

        if(difference - MARGIN > deccel)
            return maintain.set(destination).sub(ownerP.p).setLength(pm.thrust);
        else
            return maintain.set(ownerP.v).scl(-1f).setLength(pm.thrust);
    }
}
