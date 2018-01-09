package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Seek{

    private static Vector2 seek = new Vector2();

    // Injected from ShipAI
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;

    public static Vector2 calc(int owner, int target){
        seek.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p).setLength(mPowered.get(owner).thrust);
        return seek;
    }
}
