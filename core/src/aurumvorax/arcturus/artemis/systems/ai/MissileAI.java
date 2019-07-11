package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Missile;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class MissileAI extends IteratingSystem{

    private static Vector2 deltaP = new Vector2();
    private static Vector2 deltaV = new Vector2();

    private static ComponentMapper<Missile> mMissile;
    private static ComponentMapper<PoweredMotion> mPowered;
    private static ComponentMapper<Physics2D> mPhysics;


    public MissileAI(){
        super(Aspect.all(Missile.class, PoweredMotion.class, Physics2D.class));
    }

    @Override
    protected void process(int entityId){
        PoweredMotion pm = mPowered.get(entityId);
        Physics2D pMissile = mPhysics.get(entityId);
        Missile m = mMissile.get(entityId);

        pm.accel.set(pm.thrust, 0).rotate(pMissile.theta);

        if((m.target != -1) && (mPhysics.has(m.target))){
            Physics2D pTarget = mPhysics.get(m.target);

            float angleT = Utils.normalize(deltaP.set(pTarget.p).sub(pMissile.p).angle());
            float angleV = Utils.normalize(deltaV.set(pTarget.v).add(pMissile.v).angle());

            float difference = Utils.normalize(angleT - angleV);
            float commanded;
            if(Math.abs(difference) < 90)
                commanded = Utils.normalize(angleT + difference);
            else
                commanded = angleT;

            pMissile.omega = Utils.normalize(commanded - pMissile.theta) * 4f;

        }else{  // Straighten out and fly off into the sunset
            pm.alpha = -mPhysics.get(entityId).omega;
        }
    }
}
