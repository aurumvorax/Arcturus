package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

public class Movement extends IteratingSystem{

    private static final float V_CUTOFF = 10f;
    private static final float O_CUTOFF = 1f;

    private ComponentMapper<Physics2D> mPhysics;
    private ComponentMapper<PoweredMotion> mPowered;

    public Movement(){
        super(Aspect.all(Physics2D.class));
    }

    @Override
    protected void process(int entityId){
        Physics2D physics2D = mPhysics.get(entityId);
        float delta = world.delta;

        physics2D.p.mulAdd(physics2D.v, delta);
        physics2D.theta = Utils.normalize(physics2D.theta + (physics2D.omega * delta));

        if(mPowered.has(entityId)){
            PoweredMotion p = mPowered.get(entityId);

            if(!p.accel.isZero()){
                if(!physics2D.v.isZero() && (physics2D.v.len2() <= V_CUTOFF))
                    physics2D.v.setZero();
                else{
                    physics2D.v.mulAdd(p.accel, delta);
                    if(physics2D.v.len2() >= p.maxV * p.maxV)
                        physics2D.v.setLength2(p.maxV * p.maxV);
                }
            }

            if((physics2D.omega != 0) && (Math.abs(physics2D.omega) <= O_CUTOFF))
                physics2D.omega = 0;
            else
                physics2D.omega = MathUtils.clamp(-p.maxO, physics2D.omega + (p.alpha * delta), p.maxO);
        }
    }
}
