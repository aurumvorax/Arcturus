package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.utils.IntMap;

public class PlayerShip extends BaseEntitySystem{

    @EntityId private static int playerID;
    @EntityId private static int targetID;
    private static String name;
    public static String type;
    private static float x, y, theta;
    public static IntMap<String> weapons = new IntMap<>();


    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;

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

    public static void extract(){
        Ship ship = mShip.get(playerID);
        name = ship.name;
        type = ship.type;

        Physics2D physics = mPhysics.get(playerID);
        x = physics.p.x;
        y = physics.p.y;
        theta = physics.theta;

        weapons.clear();
        Weapons w = mWeapons.get(playerID);
        for(int i = 0; i < w.all.size(); i++)
        {
            Weapon weapon = getWeapon(w.all.get(i));
            weapons.put(weapon.slot, weapon.name);
        }

        Destructor.safeRemove(playerID);
    }

    public static void insert(){
        int ship =  ShipFactory.create(type, null, x, y, theta);
        mPlayer.create(ship);
        mShip.get(ship).name = name;
        ShipFactory.equip(ship, ShipFactory.getShipData(type), weapons);
    }

    private static Weapon getWeapon(int weaponID){
        if(mBeam.has(weaponID))
            return mBeam.get(weaponID);
        if(mCannon.has(weaponID))
            return mCannon.get(weaponID);

        throw new IllegalArgumentException(weaponID + " is not a valid weapon type");
    }



    private static class WeaponData{
        String type;
        int slot;
        // TODO fire control group
    }
}
