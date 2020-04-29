package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

class Detection implements Scheduled{

    private static EntitySubscription shipSub;
    private static EntitySubscription missileSub;

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Faction> mFaction;
    private static ComponentMapper<AIData> mData;


    public void init(World world){
        world.inject(this);

        shipSub = world.getAspectSubscriptionManager().get(Aspect.all(Ship.class, Sensors.class, Physics2D.class));
        missileSub = world.getAspectSubscriptionManager().get(Aspect.all(MissileTargeting.class, Physics2D.class));
    }

    @Override
    public void runTask(int ship){
        if(!mSensors.has(ship) || (!mPhysics.has(ship) || !mData.has(ship)))
            return;

        Sensors s = mSensors.get(ship);
        s.reset();

        Vector2 pos = mPhysics.get(ship).p;
        float closestDst2 = Float.MAX_VALUE;
        int closestID = -1;

        for(int i = 0; i < shipSub.getEntities().size(); i++){
            int target = shipSub.getEntities().get(i);

            if(ship == target || !mSensors.has(target))
                return;

            float dst2 = pos.dst2(mPhysics.get(target).p);
            float range = s.sensorPower * mSensors.get(target).visibility;
            if((mSensors.get(target).beacon) || (range * range > dst2)){

                switch(getFaction(ship, target)){
                    case 0:   // Friendly
                        s.friendlyShips.add(target);
                        break;

                    case 1:   // Hostile
                        s.hostileShips.add(target);
                        if(dst2 < closestDst2){
                            closestDst2 = dst2;
                            closestID = target;
                        }
                        break;
                }
            }

            if(closestID != -1){
                AIData ai = mData.get(ship);
                ai.targetID = closestID;
                ai.threatRange2 = closestDst2;
            }
        }

        for(int i = 0; i < missileSub.getEntities().size(); i++){
            int target = missileSub.getEntities().get(i);

            if(!mProjectile.has(target) || !mPhysics.has(target))
                return;

            float dst2 = pos.dst2(mPhysics.get(target).p);
            if((s.sensorPower * s.sensorPower) > dst2 && getFaction(ship, mProjectile.get(target).firedFrom) == 1)
                s.nonfriendlyMissiles.add(target);
        }
    }

    private int getFaction(int ship, int target){
        // TODO - make actual faction manager
        return (mFaction.get(ship).faction.equals(mFaction.get(target).faction)) ? 0 : 1;
    }
}
