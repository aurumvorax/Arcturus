package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.factories.ProjectileFactory;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.Weapons;
import aurumvorax.arcturus.Utils;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WeaponsUpdate extends IteratingSystem{

    private static final float DAMPING = 10f;

    private static transient Vector2 targetVector = new Vector2();
    private static transient Vector2 origin = new Vector2();

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Launcher> mLauncher;


    public WeaponsUpdate(){
        super(Aspect.all(Weapons.class));
    }

    @Override
    protected void process(int entityID){
        Weapons weapons = mWeapons.get(entityID);

        for(int i = 0; i < weapons.all.size(); i++){
            int weapon = weapons.all.get(i);

            Mounted m = mMounted.get(weapon);
            Turret t = mTurret.get(weapon);

            updateTurret(m, t);

            if(mBeam.has(weapon)){
                Beam b = mBeam.get(weapon);
                updateBeam(b, m, t);

            }else if(mCannon.has(weapon)){
                Cannon c = mCannon.get(weapon);
                updateCannon(c, m, t);

            }else if(mLauncher.has(weapon)){
                Launcher l = mLauncher.get(weapon);
                updateCannon(l, m, t);
            }
        }
    }

    private void updateTurret(Mounted m, Turret t){
        Physics2D parent = mPhysics.get(m.parent);

        m.position.set(m.location).rotate(parent.theta).add(parent.p);

        float targetAngle;

        if(t.targetPosition != null){
            targetVector.set(t.targetPosition).sub(m.position);
            targetAngle = Utils.normalize(targetVector.angle());

        }else
            targetAngle = parent.theta + t.arcMid;

        float zeroAngle = parent.theta + t.arcMid;
        float targetRelative = Utils.normalize(targetAngle - zeroAngle);
        float thetaRelative = Utils.normalize(m.theta - zeroAngle);
        float omega = MathUtils.clamp((targetRelative - thetaRelative) * DAMPING, -t.omegaMax, t.omegaMax) + parent.omega;

        thetaRelative += omega * world.delta;
        thetaRelative = MathUtils.clamp(thetaRelative, t.arcMin, t.arcMax);
        m.theta = thetaRelative + zeroAngle;

        t.sweepMin = Utils.normalize(parent.theta + t.arcMin);
        t.sweepMax = Utils.normalize(parent.theta + t.arcMax);
    }

    private void updateBeam(Beam b, Mounted m, Turret t){
        b.unitBeam.setAngle(m.theta);

        if(t.fire){
            b.length = b.range;
            b.origin.set(b.barrels.get(0)).rotate(m.theta).add(m.position);
            b.active = true;
            return;
        }

        b.active = false;
    }

    private void updateCannon(Cannon c, Mounted m, Turret t){
        c.timer -= world.delta;

        if((c.timer < 0) && ((t.fire) || (c.barrel != 0))){
            origin.set(c.barrels.get(c.barrel)).rotate(m.theta).add(m.position);

            if(c.launches != null){
                if(c instanceof Launcher)
                    ProjectileFactory.createMissile(c.launches, origin.x, origin.y, m.theta, m.parent, t.target);
                else
                    ProjectileFactory.createBullet(c.launches, origin.x, origin.y, m.theta, m.parent);

                if(c.barrel == c.barrels.size - 1){  //last barrel in sequence
                    c.timer = c.reloadTime;
                    c.barrel = 0;

                }else{
                    c.timer = c.burstTime;
                    c.barrel++;
                }
            }
        }
    }
}
