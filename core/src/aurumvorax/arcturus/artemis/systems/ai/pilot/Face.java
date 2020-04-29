package aurumvorax.arcturus.artemis.systems.ai.pilot;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Face{

    private static final float TOLERANCE = 1;
    private static Vector2 temp = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;


    public static float heading(int owner){
        temp.set(mPhysics.get(owner).v);

        if(temp.isZero())
            return stop(owner);
        else
            return absolute(owner, temp.angle());
    }

    public static float target(int owner, int target){
        if(!mPhysics.has(target))
            return 0;

        temp.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p);
        return absolute(owner, temp.angle());
    }

    public static float relative(int owner, int target, float offset){
        if(!mPhysics.has(target))
            return 0;

        temp.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p);
        return absolute(owner, temp.angle() + offset);
    }

    public static float absolute(int owner, float angle){
        Physics2D ownerP = mPhysics.get(owner);
        float error = Utils.normalize(angle - ownerP.theta);

        if(Math.abs(error) <= TOLERANCE){
            return stop(owner);
        }

        PoweredMotion pm = mPowered.get(owner);

        if(Math.abs(error) > ownerP.omega * ownerP.omega * 0.5f / pm.rotation)
            return Utils.sign(error) * pm.rotation;
        else
            return stop(owner);
    }

    public static float stop(int owner){
        return -Utils.sign(mPhysics.get(owner).omega) * mPowered.get(owner).rotation;
    }

}
