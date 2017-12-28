package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.AIShip;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.ai.ShipProxy;
import aurumvorax.arcturus.artemis.systems.ai.TargetProxy;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

public class AIControl extends BaseEntitySystem{

    private IntMap<ShipProxy> shipList;
    Arrive<Vector2> steering = new Arrive<>(null);
    SteeringAcceleration<Vector2> navOrders = new SteeringAcceleration<Vector2>(new Vector2());
    TargetProxy convergence = new TargetProxy();


    private static ComponentMapper<PlayerShip> mPlayer;
    private static ComponentMapper<PoweredMotion> mPowered;

    public AIControl(){
        super(Aspect.all(AIShip.class, Physics2D.class));
        shipList = new IntMap<>();
    }

    @Override
    public void initialize(){
        world.inject(new ShipProxy(-1));
    }


    @Override
    protected void processSystem(){
        for(ShipProxy ship : shipList.values()){
            steering.setOwner(ship);
            steering.setTarget(convergence);
            steering.calculateSteering(navOrders);
            PoweredMotion pm = mPowered.get(ship.getShipID());
            pm.accel.set(navOrders.linear);
            pm.alpha = navOrders.angular;
        }
    }

    @Override
    protected void inserted(int entityID){
        if(!mPlayer.has(entityID))
            shipList.put(entityID, new ShipProxy(entityID));
    }

    @Override
    protected void removed(int entityID){
        shipList.remove(entityID);
    }
}
