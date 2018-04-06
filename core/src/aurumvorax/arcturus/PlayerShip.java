package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import com.artemis.ComponentMapper;
import com.artemis.World;

public class PlayerShip{

    private static final PlayerShip INSTANCE = new PlayerShip();

    private String name;
    private String type;

    private float x, y, theta;

    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Weapons> mWeapons;

    public static void init(World world){
        world.inject(INSTANCE);
    }

    public void extract(int shipID){
        Ship ship = mShip.get(shipID);
        name = ship.name;
        type = ship.type;

        Physics2D physics = mPhysics.get(shipID);
        x = physics.p.x;
        y = physics.p.y;
        theta = physics.theta;

        Destructor.safeRemove(shipID);
    }

    public void insert(){
        int ship =  ShipFactory.create(type, null, x, y, theta);
        mShip.get(ship).name = name;
    }
}
