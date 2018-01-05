package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

class Navigation{


    private static Vector2 seek = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<PoweredMotion> mPowered;

    static void execute(int shipID, Vector2 steer){
        PoweredMotion pm = mPowered.get(shipID);
        pm.accel.set(steer);
    }

    static Vector2 Seek(int owner, int target){
        seek.set(mPhysics.get(target).p).sub(mPhysics.get(owner).p).setLength(mPowered.get(owner).thrust);
        return seek;
    }
}
