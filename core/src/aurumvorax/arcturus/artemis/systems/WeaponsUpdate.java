package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.factories.ProjectileFactory;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.Utils;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WeaponsUpdate extends IteratingSystem{

    private static final float DAMPING = 10;
    private static final float AIM_FUZZ = 10;
    private static transient float targetAngle;
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

        for(int i = 0; i < w.all.size(); i++){
            int weapon = w.all.get(i);
            Turret t = mTurret.get(weapon);

            if(t.target == null)
                targetAngle = t.arcMid;
            else
                updateActive(weapon, t);
            updateTurret(weapon);
        }
    }

    private void updateActive(int weapon, Turret t){
        Mounted m = mMounted.get(weapon);
        targetVector.set(t.target).sub(m.position);
        targetAngle = Utils.normalize(targetVector.angle());

        if(mCannon.has(weapon))
            updateCannon(mCannon.get(weapon), m, t.fire);
        if(mBeam.has(weapon))
            updateBeam(mBeam.get(weapon), m, t.fire);
    }

    private void updateTurret(int weapon){
        Mounted m = mMounted.get(weapon);
        Turret t = mTurret.get(weapon);
        Physics2D parent = mPhysics.get(m.parent);

        m.position.set(m.location).rotate(parent.theta).add(parent.p);

        float zeroAngle = parent.theta + t.arcMid;
        float targetRelative = Utils.normalize(targetAngle - zeroAngle);
        float thetaRelative = Utils.normalize(m.theta - zeroAngle);
        float omega = MathUtils.clamp((targetRelative - thetaRelative) * DAMPING, -t.omegaMax, t.omegaMax) + parent.omega;
        thetaRelative += omega * world.delta;
        thetaRelative = MathUtils.clamp(thetaRelative, t.arcMin, t.arcMax);
        m.theta = thetaRelative + zeroAngle;
        t.onTarget = (Math.abs(omega) < AIM_FUZZ);
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
        if(cannon.timer <= -cannon.reloadTime){
            cannon.barrel = 0;
            cannon.timer = 0;
        }
    }

    private void updateBeam(Beam beam, Mounted m, boolean fire){
        if(fire){
            beam.length = beam.maxRange;
            beam.unitBeam.setAngle(m.theta);
            beam.origin.set(beam.barrels.get(0)).rotate(m.theta).add(m.position);
        }
        beam.active = fire;
    }
}
