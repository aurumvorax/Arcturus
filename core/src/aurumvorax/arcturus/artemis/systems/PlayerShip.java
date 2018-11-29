package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.PlayerData;
import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.Cannon;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Weapon;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import aurumvorax.arcturus.artemis.factories.ShipFactory;
import aurumvorax.arcturus.backstage.Profiles;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;

import static aurumvorax.arcturus.PlayerData.y;

public class PlayerShip extends BaseEntitySystem{

    @EntityId private static int playerID;
    @EntityId private static int targetID;


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

    static void extract(){
        Physics2D physics = mPhysics.get(playerID);
        PlayerData.x = physics.p.x;
        y = physics.p.y;
        PlayerData.t = physics.theta;

        Ship ship = mShip.get(playerID);
        Profiles.Ship profile = new Profiles.Ship(ship.name, ship.type, null);

        Weapons w = mWeapons.get(playerID);
        for(int i = 0; i < w.all.size(); i++){
            Weapon weapon = getWeapon(w.all.get(i));
            profile.loadout.weapons.put(weapon.slot, weapon.name);
        }

        PlayerData.SetPlayerShip(profile);

        Destructor.safeRemove(playerID);
    }

    static void insert(){
        int ship =  ShipFactory.create(PlayerData.GetPlayerShip(), PlayerData.x, PlayerData.y, PlayerData.t);
        mPlayer.create(ship);
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
