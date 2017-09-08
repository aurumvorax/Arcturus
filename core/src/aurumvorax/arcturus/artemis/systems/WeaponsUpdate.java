package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Turret;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WeaponsUpdate extends IteratingSystem{


    private static transient Vector2 targetVector = new Vector2();

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Physics2D> mPhysics;


    public WeaponsUpdate(){
        super(Aspect.all(Weapons.class));
    }


    @Override
    protected void process(int entityID){
        Weapons w = mWeapons.get(entityID);
        for(int weapon : w.active.toArray())
            updateActive(weapon, w.target, w.fire);
        for(int weapon : w.all.toArray())
            updateWeapon(weapon);
    }

    private void updateActive(int weapon, Vector2 target, boolean fire){
        Turret t = mTurret.get(weapon);
        Physics2D p = mPhysics.get(mMounted.get(weapon).parent);
        Mounted m = mMounted.get(weapon);

        if(target == null)
            t.target = t.arcMid;
        else{
            targetVector.set(target).sub(p.p);
            t.target = targetVector.angle();
        }
    }

    private void updateWeapon(int weapon){
        Mounted m = mMounted.get(weapon);
        Turret t = mTurret.get(weapon);
        Physics2D parent = mPhysics.get(m.parent);

        m.position.set(m.location).rotate(parent.theta).add(parent.p);
        float zeroAngle = parent.theta + t.arcMid;
        float targetAngle = normalize(t.target - zeroAngle);
        float thetaRelative = normalize(m.theta - zeroAngle);
        float omega = MathUtils.clamp((targetAngle - thetaRelative) * 10, -t.omegaMax, t.omegaMax);
        thetaRelative += omega * world.delta;
        thetaRelative = MathUtils.clamp(thetaRelative, t.arcMin, t.arcMax);
        m.theta = thetaRelative + zeroAngle;

    }

    private float normalize(float angle){
        while(angle < -180) angle += 360;
        while(angle > 180) angle -= 360;
        return angle;
    }
}
