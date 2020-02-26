package aurumvorax.arcturus.artemis.systems.ai;


import aurumvorax.arcturus.artemis.components.*;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

class Detection{

    private static EntitySubscription shipSub;
    private static EntitySubscription missileSub;

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Missile> mMissile;
    private static ComponentMapper<Faction> mFaction;


    void init(World world){
        world.inject(this);

        shipSub = world.getAspectSubscriptionManager().get(Aspect.all(Ship.class, Sensors.class, Physics2D.class));
        missileSub = world.getAspectSubscriptionManager().get(Aspect.all(Missile.class, Physics2D.class));

    }

    void process(int ship){
        if(!mSensors.has(ship) || (!mPhysics.has(ship)))
            return;

        Vector2 pos = mPhysics.get(ship).p;
        Sensors s = mSensors.get(ship);
        s.reset();

        for(int i = 0; i < shipSub.getEntities().size(); i++){
            int target = shipSub.getEntities().get(i);

            if(!mSensors.has(ship))
                return;

            if(mSensors.get(target).beacon || isDetectable(pos, s, target))
                sortShip(ship, s, target);
        }

        for(int i = 0; i < missileSub.getEntities().size(); i++){
            int target = missileSub.getEntities().get(i);

            if(!mMissile.has(target) || !mPhysics.has(target))
                return;

            float dst2 = pos.dst2(mPhysics.get(target).p);
            if((s.sensorPower * s.sensorPower) > dst2)
                s.nonfriendlyMissiles.add(target);
        }
    }

    private boolean isDetectable(Vector2 shipPos, Sensors s, int targetShip){
        float range = s.sensorPower * mSensors.get(targetShip).visibility;
        float dst2 = shipPos.dst2(mPhysics.get(targetShip).p);
        return (range * range) > dst2;
    }

    private void sortShip(int ship, Sensors s, int targetShip){
        //TODO hook this to faction manager, once I build faction manager

        if(mFaction.get(ship).faction.equals(mFaction.get(targetShip).faction))
            s.friendlyShips.add(targetShip);
        else
            s.hostileShips.add(targetShip);
    }
}
