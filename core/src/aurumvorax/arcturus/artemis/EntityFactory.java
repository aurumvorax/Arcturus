package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public enum EntityFactory{
    INSTANCE;

    private static World world;
    private static ComponentMapper<Position> mPosition;
    private static ComponentMapper<Velocity> mVelocity;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<CollisionSimple> mCollidable;
    private static ComponentMapper<Inertia>  mInertia;
    private static ComponentMapper<CollisionPolygon> mPolygon;

    public static void init(World world){
        EntityFactory.world = world;
        mPosition = world.getMapper(Position.class);
        mVelocity = world.getMapper(Velocity.class);
        mSprite = world.getMapper(SimpleSprite.class);
        mCollidable = world.getMapper(CollisionSimple.class);
        mInertia = world.getMapper(Inertia.class);
        mPolygon = world.getMapper(CollisionPolygon.class);
    }

    public static int createShip(float x, float y, float theta){
        int ship = world.create();
        mPosition.create(ship).p.set(x, y);
        mPosition.get(ship).theta = theta;
        mVelocity.create(ship);
        mSprite.create(ship).name = Services.SHIP_IMG_PATH + "TestShip";
        mSprite.get(ship).offsetX = 64;
        mSprite.get(ship).offsetY = 32;
        mCollidable.create(ship).radius = 70;
        mInertia.create(ship);
        Array<Vector2> temp = new Array<>();
        temp.add(new Vector2(60, 28));
        temp.add(new Vector2(60, -28));
        temp.add(new Vector2(-60, -28));
        temp.add(new Vector2(-60, 28));
        Array<Array<Vector2>> bob = new Array<>();
        bob.add(temp);
        mPolygon.create(ship).setVertices(bob);


        return ship;
    }
}
