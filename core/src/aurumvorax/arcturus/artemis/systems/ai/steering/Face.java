package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class Face{
    private static Face INSTANCE = new Face();
    private Face(){} // Single static class with DI/callback

    private static final float TOLERANCE = 1;
    private static Vector2 working = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;


    public static void initialize(World world){ world.inject(INSTANCE); }

    public static float heading(int owner){
        working.set(mPhysics.get(owner).v);

        if(working.isZero())        // If we are stopped, just stop our spin
            return stop(owner);
        else
            return absolute(owner, working.angle());
    }

    public static float target(int owner, int target){
        if(!mPhysics.has(target))
            return 0;
        working.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p);
        return absolute(owner, working.angle());
    }

    public static float relative(int owner, int target, float offset){
        if(!mPhysics.has(target))
            return 0;
        working.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p);
        return absolute(owner, working.angle() + offset);
    }

    public static float absolute(int owner, float angle){
        Physics2D ownerP = mPhysics.get(owner);
        float error = Utils.normalize(angle - ownerP.theta);

        if(Math.abs(error) <= TOLERANCE){    // We have reached the angle we want
            return stop(owner);
        }
        PoweredMotion pm = mPowered.get(owner);
        if(Math.abs(error) > ownerP.omega * ownerP.omega * 0.5f / pm.rotation){     // Not close enough to slow down the turn yet
            return Utils.sign(error) * pm.rotation;
        }else{                                                                  // Slow our rotation
            return stop(owner);
        }
    }

    public static float stop(int owner){
        float omega = mPhysics.get(owner).omega;
        return -Utils.sign(omega) * mPowered.get(owner).rotation;
    }

}
