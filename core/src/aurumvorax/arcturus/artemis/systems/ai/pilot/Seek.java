package aurumvorax.arcturus.artemis.systems.ai.pilot;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class Seek{
    private static Seek INSTANCE = new Seek();
    private Seek(){} // Single static class with DI/callback

    private static Vector2 seek = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;


    public static void initialize(World world){
        world.inject(INSTANCE);
    }

    public static Vector2 calc(int owner, int target){
        if(!mPhysics.has(target))
            return null;
        seek.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p).setLength(mPowered.get(owner).thrust);
        return seek;
    }
}
