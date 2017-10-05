package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.ProjectileFactory;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.Utils;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WeaponsUpdate extends IteratingSystem{


    private static transient Vector2 targetVector = new Vector2();
    private static transient Vector2 origin = new Vector2();

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Beam> mBeam;


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
        Mounted m = mMounted.get(weapon);

        if(target == null)
            t.target = t.arcMid;
        else{
            targetVector.set(target).sub(m.position);
            t.target = targetVector.angle();
        }
        if(mCannon.has(weapon))
            updateCannon(mCannon.get(weapon), m, fire);
        if(mBeam.has(weapon))
            updateBeam(mBeam.get(weapon), fire);

    }

    private void updateWeapon(int weapon){
        Mounted m = mMounted.get(weapon);
        Turret t = mTurret.get(weapon);
        Physics2D parent = mPhysics.get(m.parent);

        m.position.set(m.location).rotate(parent.theta).add(parent.p);
        float zeroAngle = parent.theta + t.arcMid;
        float targetAngle = Utils.normalize(t.target - zeroAngle);
        float thetaRelative = Utils.normalize(m.theta - zeroAngle);
        float omega = MathUtils.clamp((targetAngle - thetaRelative) * 10, -t.omegaMax, t.omegaMax);
        thetaRelative += omega * world.delta;
        thetaRelative = MathUtils.clamp(thetaRelative, t.arcMin, t.arcMax);
        m.theta = thetaRelative + zeroAngle;
    }

    private void updateCannon(Cannon cannon, Mounted m, boolean fire){
        cannon.timer -= world.delta;
        if(fire && cannon.timer <= 0){
            origin.set(cannon.barrels.get(cannon.barrel)).rotate(m.theta).add(m.position);
            if(cannon.launches != null)
                ProjectileFactory.create(cannon.launches, origin.x, origin.y, m.theta, m.parent);
            if(cannon.barrel == cannon.barrels.size - 1){  //last barrel in sequence
                cannon.timer = cannon.reloadTime;
                cannon.barrel = 0;
            }else{
                cannon.timer = cannon.burstTime;
                cannon.barrel++;
            }
        }
    }

    private void updateBeam(Beam beam, boolean fire){
        if(fire)
            beam.length = beam.maxRange;
        beam.active = fire;
    }
}
