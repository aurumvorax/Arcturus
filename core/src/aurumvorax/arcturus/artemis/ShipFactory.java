package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import java.util.HashMap;

public class ShipFactory{

    private static final ShipFactory INSTANCE = new ShipFactory();
    private static World world;
    private static HashMap<String, ShipData> ships;
    private static Archetype protoShip;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mCollision;
    private static ComponentMapper<CollisionPolygon> mPolygon;
    private static ComponentMapper<Inertia> mInertia;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Weapons> mWeapons;

    public static void init(World world){
        ShipFactory.world = world;
        world.inject(INSTANCE);
        protoShip = new ArchetypeBuilder()
                .add(Ship.class)
                .add(Physics2D.class)
                .add(CollisionRadius.class)
                .add(CollisionPolygon.class)
                .add(Inertia.class)
                .add(SimpleSprite.class)
                .add(Weapons.class)
                .build(world);
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

        int ship = world.create(protoShip);
        ShipData data = ships.get(type);

        Physics2D p = mPhysics.get(ship);
        p.p.set(x, y);
        p.theta = t;

        mCollision.get(ship).radius = data.collisionRadius;
        mPolygon.get(ship).setVertices(data.vertices);

        SimpleSprite s = mSprite.get(ship);
        s.name = Services.SHIP_IMG_PATH + data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        IntArray weaponList = mWeapons.get(ship).all;
        IntArray activeList = mWeapons.get(ship).active;

        IntMap<String> loadout =  data.variants.get(variant).weapons;
        if(loadout != null){
            for(int i = 0; i < loadout.size; i++){
                int w = WeaponFactory.create(loadout.get(i), ship, data.weaponMounts.get(i));
                weaponList.add(w);
                activeList.add(w);
            }
        }



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
        Array<Mount.Weapon> weaponMounts;
        HashMap<String, Variant> variants;
    }

    private static class Variant{
        IntMap<String> weapons;
    }
}
