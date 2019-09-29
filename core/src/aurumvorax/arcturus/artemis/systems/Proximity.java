package aurumvorax.arcturus.artemis.systems;


import aurumvorax.arcturus.artemis.components.*;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Proximity extends IteratingSystem{

    private static EntitySubscription shipSub;
    private static EntitySubscription missileSub;
    private static EntitySubscription celestialSub;

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Missile> mMissile;
    private static ComponentMapper<Celestial> mCelestial;


    public Proximity(){
        super(Aspect.all(Physics2D.class, Ship.class, Sensors.class));
    }

    public void initialize(){
        shipSub = world.getAspectSubscriptionManager().get(Aspect.all(Ship.class, Sensors.class));
        missileSub = world.getAspectSubscriptionManager().get(Aspect.all(Missile.class));
        celestialSub = world.getAspectSubscriptionManager().get(Aspect.all(Celestial.class));
    }

    @Override
    protected void process(int entityId){
        if(!mSensors.has(entityId) || !mPhysics.has(entityId))
            return;

        Sensors s = mSensors.get(entityId);
        s.ships.clear();
        s.missiles.clear();

        Vector2 position = mPhysics.get(entityId).p;
        float range2 = s.range * s.range;

        IntBag ships = shipSub.getEntities();
        for(int ship : ships.getData()){
            if(mSensors.has(ship) && mSensors.get(ship).beacon){
                s.ships.add(ship);
                continue;
            }
            if((mPhysics.has(ship)) && (position.dst2(mPhysics.get(ship).p) > range2))
                ships.add(ship);
        }

        if(!s.scanForMissiles)
            return;

        IntBag missiles = missileSub.getEntities();
        for(int missile : missiles.getData()){
            if((mPhysics.has(missile) && (position.dst2(mPhysics.get(missile).p) > range2)))
                missiles.add(missile);
        }
    }
}
