package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Missile;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class MissileAI extends IteratingSystem{

    private static ComponentMapper<Missile> mMissile;
    private static ComponentMapper<PoweredMotion> mPowered;
    private static ComponentMapper<Physics2D> mPhysics;


    public MissileAI(){
        super(Aspect.all(Missile.class, PoweredMotion.class, Physics2D.class));
    }

    @Override
    protected void process(int entityId){
        PoweredMotion pm = mPowered.get(entityId);
        Missile m = mMissile.get(entityId);
        float theta = mPhysics.get(entityId).theta;
        pm.accel.set(pm.thrust, 0).rotate(theta);
        if((m.target != -1) && (mPhysics.has(m.target))){
            Vector2 deltaP = new Vector2(mPhysics.get(m.target).p).sub(mPhysics.get(entityId).p);
            pm.alpha = Utils.normalize(deltaP.angle() - theta) * 5000;
        }

    }
}
