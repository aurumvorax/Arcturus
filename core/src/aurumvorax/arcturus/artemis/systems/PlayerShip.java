package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;

public class PlayerShip extends BaseEntitySystem{

    @EntityId private static int playerID;
    @EntityId private static int targetID;

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Physics2D> mPhysics;

    public PlayerShip(){
        super(Aspect.all(Player.class));
    }

    @Override protected void processSystem(){}
    @Override protected void inserted(int playerShip){ playerID = playerShip; }

    public static int getID(){ return playerID; }
    public static void setTargetID(int target){ targetID = target; }

    public static int getTargetID(){
        if(mPlayer.has(playerID))
            if((targetID != -1) && (mPhysics.has(targetID)))
                return targetID;
        return -1;
    }

    static void extract(){
        PlayerData.SetPlayerShip(ShipFactory.extract(playerID));
        Destructor.safeRemove(playerID);
    }

    static void insert(){
        int ship = ShipFactory.create(PlayerData.GetPlayerShip());
        mPlayer.create(ship);
    }
}
