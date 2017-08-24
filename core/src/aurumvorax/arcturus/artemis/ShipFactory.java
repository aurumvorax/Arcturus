package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.systems.Renderer;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;

public class ShipFactory{

    private static World world;
    private static HashMap<String, ShipData> ships;

    private static ComponentMapper<Position> mPosition;
    private static ComponentMapper<Velocity> mVelocity;
    private static ComponentMapper<CollisionRadius> mCollision;
    private static ComponentMapper<CollisionPolygon> mPolygon;
    private static ComponentMapper<Inertia> mInertia;
    private static ComponentMapper<SimpleSprite> mSprite;

    public ShipFactory(World world){
        ShipFactory.world = world;
        world.inject(ShipFactory.class);
        ships = new HashMap<>();
        for(FileHandle entry : Services.SHIP_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            ships.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Ship - " + wrapper.name);
        }
    }

    public static int create(String type, String variant, float x, float y, float t){
        if(!ships.containsKey(type))
            throw new IllegalArgumentException("Invalid ship type - " + type);
        if(!ships.get(type).variants.containsKey(variant))
            throw new IllegalArgumentException("Invalid ship variant - " + type + " - " + variant);

        int ship = world.create();
        ShipData data = ships.get(type);

        Position p = mPosition.create(ship);
        p.p.set(x, y);
        p.theta = t;

        mVelocity.create(ship);
        mInertia.create(ship);
        mCollision.create(ship).radius = data.collisionRadius;
        mPolygon.create(ship).setVertices(data.vertices);

        SimpleSprite s = mSprite.create(ship);
        s.name = Services.SHIP_IMG_PATH + data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        return ship;
    }

    private static class Wrapper{
        String name;
        ShipData data;
    }

    private static class ShipData{
        String imgName;
        Vector2 imgCenter;
        int collisionRadius;
        Array<Array<Vector2>> vertices;
        HashMap<String, Variant> variants;
    }

    private static class Variant{
        IntMap<String> weapons;
    }


}
