package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public enum Arrive{
    INSTANCE;

    private static Vector2 arrive = new Vector2();

    // Injected from ShipAI
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;

    public static Vector2 calc(int owner, int target, float margin){
        Physics2D ownerP = mPhysics.get(owner);
        Physics2D targetP = mPhysics.get(target);
        float distance = ownerP.p.dst(targetP.p);
        if(distance <= margin)  // Already there
            return Stop.calc(owner);
        PoweredMotion pm = mPowered.get(owner);
        float deccel = ownerP.v.len2() * 0.5f /  pm.thrust;
        if(distance - margin > deccel)   // Not close enough to slow down yet
            return arrive.set(targetP.p).sub(ownerP.p).setLength(pm.thrust);
        return arrive.set(ownerP.v).scl(-1f).setLength(pm.thrust);
    }
}
