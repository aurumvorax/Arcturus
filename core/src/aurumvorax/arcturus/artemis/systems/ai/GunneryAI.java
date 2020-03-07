package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class GunneryAI{

    private static final float FIRING_FUDGE = 0.8f;
    private static final float MISSILE_FUDGE = 0.5f;

    private static transient Vector2 targetPosition = new Vector2();
    private static transient Vector2 relativeV = new Vector2();
    private static transient Vector2 unitVector = new Vector2(1, 0);

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Mounted> mMounted;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Missile> mMissile;

    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Launcher> mLauncher;



    public void process(int ship){/*
        Weapons w = mWeapons.get(ship);
        Detection s = mSensors.get(ship);

        if(!mPlayer.has(ship)){
            for(int i = 0; i < w.main.size(); i++){
                processWeapon(w.main.get(i), s);
            }
        }

        for(int i = 0; i < w.auto.size(); i++){
            processWeapon(w.auto.get(i), s);
        }

        for(int i = 0; i < w.pd.size(); i++){
            processWeapon(w.pd.get(i), s);
        }
    }

    private void processWeapon(int weapon, Detection s){
        Turret t = mTurret.get(weapon);

        t.fire = false;

        if(t.target == -1){
            t.targetPosition = null;
            return;
        }

        if(!mShip.has(t.target) && !mMissile.has(t.target)){
            t.target = -1;
            t.targetPosition = null;
            return;
        }

        Mounted m = mMounted.get(weapon);
        Physics2D targetPhysics = mPhysics.get(t.target);
        float dst2 = m.position.dst2(targetPhysics.p);

        if(mBeam.has(weapon)){
            t.targetPosition.set(targetPhysics.p);

            float range = mBeam.get(weapon).range;
            if(dst2 > range * range){
                t.target = -1;
                return;
            }

        }else if(mCannon.has(weapon)){
            Cannon c = mCannon.get(weapon);
            float time = (float)Math.sqrt(dst2) / c.speed;
            relativeV.set(targetPhysics.v).sub(mPhysics.get(m.parent).v);

            t.targetPosition = targetPosition.set(targetPhysics.p).mulAdd(relativeV, time);

            float range = c.speed * c.duration;
            if(dst2 > range * range){
                t.target = -1;
                return;
            }


        }else if(mLauncher.has(weapon)){
            Launcher l = mLauncher.get(weapon);
            float time = (float)Math.sqrt(dst2) / l.speed;
            relativeV.set(targetPhysics.v).sub(mPhysics.get(m.parent).v);

            t.targetPosition = targetPosition.set(targetPhysics.p).mulAdd(relativeV, time);

            float range = l.speed * l.duration;
            if(dst2 > range * range){
                t.target = -1;
                return;
            }
        }

        float targetAngle = m.position.angle(t.targetPosition);
        if(!Utils.withinAngle(targetAngle, t.sweepMin, t.sweepMax)){
            t.target = -1;
            return;
        }

        float arc = getArc(dst2, mRadius.get(t.target).radius) * FIRING_FUDGE;
        if(mLauncher.has(weapon) && mLauncher.get(weapon).missileTurnRate > 0)      // Extra aim fuzz for launchers
            arc *= MISSILE_FUDGE * mLauncher.get(weapon).missileTurnRate;           // with guided projectiles

        if(!Utils.withinAngle(m.theta, targetAngle - arc, targetAngle + arc)){
            return;
        }

        dst2 = m.position.dst2(t.targetPosition);

        for(int i = 0; i < s.friendlyShips.size(); i++){
            if(checkBlocker(s.friendlyShips.get(i), m, dst2))
                return;
        }

        for(int i = 0; i < s.neutralShips.size(); i++){
            if(checkBlocker(s.neutralShips.get(i), m, dst2))
                return;
        }

        t.fire = true;

    }

    private float getArc(float dst2, float radius){
        return (float)Math.atan2(Math.sqrt(dst2), radius) * Utils.TWOPI;
    }

    private boolean checkBlocker(int blocker, Mounted m, float dst2){
        Vector2 blockerP = mPhysics.get(blocker).p;

        if(m.position.dst2(blockerP) > dst2)
            return false;

        float angle = m.position.angle(blockerP);
        float arc = getArc(m.position.dst2(blockerP), mRadius.get(blocker).radius);

        return Utils.withinAngle(m.theta, angle - arc, angle + arc);  */
    }
}
