package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.BasicSprite;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.ComponentMapper;
import com.artemis.World;

public enum EntityFactory{
    INSTANCE;

    private static World world;
    private static ComponentMapper<Position> mPosition;
    private static ComponentMapper<Velocity> mVelocity;
    private static ComponentMapper<BasicSprite> mSprite;
    private static ComponentMapper<Player> mPlayer;

    public static void init(World world){
        EntityFactory.world = world;
        mPosition = world.getMapper(Position.class);
        mVelocity = world.getMapper(Velocity.class);
        mSprite = world.getMapper(BasicSprite.class);
        mPlayer = world.getMapper(Player.class);
    }

    public static int createShip(float x, float y, float theta){
        int ship = world.create();
        mPosition.create(ship).x = x;
        mPosition.get(ship).y = y;
        mPosition.get(ship).theta = theta;
        mVelocity.create(ship);
        mSprite.create(ship).name = Services.SHIP_IMG_PATH + "TestShip";
        mPlayer.create(ship);



        return ship;
    }
}
