package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.AIShip;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import aurumvorax.arcturus.services.EntityData;
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
    private static ComponentMapper<Faction> mFaction;


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
                .add(Faction.class)
                .build(world);
    }

    public static int create(ShipData.Unique ship){
        return create(ship.name, ship.type, ship.loadout, ship.health, ship.x, ship.y, ship.t);
    }

    // TODO - temp
    public static int create(String name, String type, String variant, float x, float y, float t){
        ShipData.Stock stock = EntityData.getShipData(type);
        ShipData.Loadout loadout = stock.loadouts.get(variant);

        return create(name, type, loadout, stock.hull, x, y, t);
    }

    private static int create(String name, String type, ShipData.Loadout loadout, float hull, float x, float y, float t){
        int shipID = world.create(protoShip);
        ShipData.Stock data = EntityData.getShipData(type);

        Ship ship = mShip.get(shipID);
        ship.name = name;
        ship.type = type;

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

        mFaction.get(shipID).faction = "Pirate";

        equip(shipID, data, loadout);

        return shipID;
    }

    private static void equip(int ship, ShipData.Stock data, ShipData.Loadout loadout){
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

    public static ShipData.Unique extract(int shipID){
        Ship s = mShip.get(shipID);
        ShipData.Unique unique = new ShipData.Unique(s.name, s.type);

        Physics2D p = mPhysics.get(shipID);
        unique.x = p.p.x;
        unique.y = p.p.y;
        unique.t = p.theta;

        Weapons w = mWeapons.get(shipID);
        for(int weaponID : w.all.getData())
            WeaponFactory.extract(unique.loadout, weaponID);

        unique.health = mHealth.get(shipID).hull;

        return unique;
    }


}
