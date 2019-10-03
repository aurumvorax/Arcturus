package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class TargetingAI{

    private static final float FUDGE_COEFFICIENT = 0.7f;

    private static transient Vector2 relativeP = new Vector2();
    private static transient Vector2 relativeV = new Vector2();
    private static IntBag shipList = new IntBag();

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Launcher> mLauncher;
    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<CollisionRadius> mRadius;


    public void process(int ship){
        Weapons weapons = mWeapons.get(ship);
        Sensors sensors = mSensors.get(ship);

        for(int i = 0; i < weapons.all.size(); i++){
            int w = weapons.all.get(i);

            Turret t = mTurret.get(w);
            if((t.target == -1) || (!mPhysics.has(t.target))){  // No target
                t.targetPosition = null;
                t.fire = false;
                continue;
            }

            if(mBeam.has(w)){               // Beams aim directly at target
                t.targetPosition = mPhysics.get(t.target).p;

                t.fire = beamFireControl(w, t.target, mRadius.get(t.target).radius, sensors);

            }else if(mCannon.has(w)){       // Cannons must lead target
                Physics2D target = mPhysics.get(t.target);
                Mounted m = mMounted.get(w);
                Cannon c = mCannon.get(w);

                relativeP.set(target.p).sub(m.position);
                relativeV.set(target.v).sub(mPhysics.get(m.parent).v);

                float distance1 = relativeP.len();
                float distance2 = relativeP.nor().dot(relativeV);

                float time = -distance1 / (distance2 - c.speed);
                t.targetPosition.set(target.p).mulAdd(relativeV, time);

            }else if(mLauncher.has(w)){     // Launchers lead targets differently than cannons, due to range and seeking

            }
        }
    }

    private boolean beamFireControl(int weapon , int target, float targetRadius, Sensors sensors){
        Mounted m = mMounted.get(weapon);
        Beam b = mBeam.get(weapon);
        Physics2D targetPhysics = mPhysics.get(target);

        float dst2 = m.position.dst2(targetPhysics.p);
        float range = b.maxRange + targetRadius;
        if(dst2  > (range * range))    // Out of range
            return false;

        relativeP.set(targetPhysics.p).sub(m.position);
        float cross = relativeP.crs(b.unitBeam);

        if(Math.abs(cross) > targetRadius * FUDGE_COEFFICIENT)      // Not aimed at target
            return false;

        float dot = relativeP.dot(b.unitBeam);
        if(dot <= 0)                          // Ignore targets behind turret
            return false;

        shipList.clear();
        shipList.addAll(sensors.friendlyShips);
        shipList.addAll(sensors.neutralShips);

        for(int i = 0; i < shipList.size(); i++){
            int nonHostile = shipList.get(i);

            Vector2 nonHostileP = mPhysics.get(nonHostile).p;
            float occultDst2 = m.position.dst2(nonHostileP);

            if(occultDst2 > dst2)             // Further away than target - cant occult
                continue;

            relativeP.set(nonHostileP).sub(m.position);
            cross = relativeP.crs(b.unitBeam);

            if(Math.abs(cross) > (mRadius.get(nonHostile).radius * FUDGE_COEFFICIENT))  // Not going to hit
                continue;

            dot = relativeP.dot(b.unitBeam);                            // Non hostile is behind turret
            if(dot <= 0)
                continue;

            return false;
        }

        return true;
    }
}
