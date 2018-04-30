package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntMap;


public class ShipFactory{

    private static final ShipFactory INSTANCE = new ShipFactory();
    private static World world;
    private static Archetype protoShip;

    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mCollision;
    private static ComponentMapper<CollisionPolygon> mPolygon;
    private static ComponentMapper<Inertia> mInertia;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Health> mHealth;


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
                .add(Health.class)
                .add(PoweredMotion.class)
                .add(AIShip.class)
                .build(world);
    }

    public static int create(String type, String variant, float x, float y, float t){
        int ship = world.create(protoShip);
        ShipData data = EntityData.getShipData(type);

        Ship nameplate = mShip.get(ship);
        nameplate.name = "Shippy McShipFace";
        nameplate.type = type;

        Physics2D p = mPhysics.get(ship);
        p.p.set(x, y);
        p.theta = t;

        mCollision.get(ship).radius = data.collisionRadius;
        mPolygon.get(ship).setVertices(data.vertices);

        SimpleSprite s = mSprite.get(ship);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        mHealth.get(ship).hull = data.hull;

        if(!data.variants.containsKey(variant))
            return ship;
        equip(ship, data, data.variants.get(variant).weapons);

        return ship;
    }

    // TODO pass Variant later instead of just weapons
    public static void equip(int ship, ShipData data, IntMap<String> loadout){
        IntBag weaponList = mWeapons.get(ship).all;
        IntBag activeList = mWeapons.get(ship).auto;
        IntBag manualList = mWeapons.get(ship).manual;
        if(loadout != null){
            for(int i = 0; i < loadout.size; i++){
                if(loadout.get(i) != null){
                    int w = WeaponFactory.create(loadout.get(i), ship, data.weaponMounts.get(i), i);
                    weaponList.add(w);
                    activeList.add(w);
                    manualList.add(w);
                }
            }
        }
    }


}
