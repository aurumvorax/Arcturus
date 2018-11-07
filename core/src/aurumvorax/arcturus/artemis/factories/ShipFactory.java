package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.utils.IntBag;


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

    public static int create(Ships.Profile profile, float x, float y, float t){
        int shipID = world.create(protoShip);
        Ships.Data data = EntityData.getShipData(profile.type);

        Ship ship = mShip.get(shipID);
        ship.name = profile.name;
        ship.type = profile.type;

        Physics2D p = mPhysics.get(shipID);
        p.p.set(x, y);
        p.theta = t;

        mCollision.get(shipID).radius = data.collisionRadius;
        mPolygon.get(shipID).setVertices(data.vertices);

        SimpleSprite s = mSprite.get(shipID);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        mHealth.get(shipID).hull = data.hull;

        equip(shipID, data, profile.loadout);

        return shipID;
    }

    public static void equip(int ship, Ships.Data data, Ships.Loadout loadout){
        IntBag weaponList = mWeapons.get(ship).all;
        IntBag activeList = mWeapons.get(ship).auto;
        IntBag manualList = mWeapons.get(ship).manual;
        if(loadout.weapons != null){
            for(int i = 0; i < loadout.weapons.size; i++){
                if(loadout.weapons.get(i) != null){
                    int w = WeaponFactory.create(loadout.weapons.get(i), ship, data.weaponMounts.get(i), i);
                    weaponList.add(w);
                    activeList.add(w);
                    manualList.add(w);
                }
            }
        }
    }


}
