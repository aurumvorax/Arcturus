package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Cannon;
import aurumvorax.arcturus.artemis.components.MountedSprite;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import aurumvorax.arcturus.artemis.components.Turret;
import aurumvorax.arcturus.artemis.systems.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class WeaponFactory{

    private static final WeaponFactory INSTANCE = new WeaponFactory();
    private static World world;
    private static HashMap<String, WeaponData> weapons;
    private static Archetype protoWeapon;
    private static Archetype protoCannon;

    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<MountedSprite> mSprite;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Cannon> mCannon;


    public static void init(World world){
        WeaponFactory.world = world;
        world.inject(INSTANCE);

        protoWeapon = new ArchetypeBuilder()
                .add(Mounted.class)
                .add(MountedSprite.class)
                .add(Turret.class)
                .build(world);
        protoCannon = new ArchetypeBuilder(protoWeapon)
                .add(Cannon.class)
                .build(world);

        weapons = new HashMap<>();
        for(FileHandle entry : Services.WEAPON_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            weapons.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Ship - " + wrapper.name);
        }
    }

    public static int create(String type, int ship, Mount.Weapon mount){

        if(!weapons.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);

        WeaponData data = weapons.get(type);

        switch(data.type){
            case CANNON:
                int cannon = world.create(protoCannon);
                buildTurret(cannon, data, ship, mount);
                Cannon c = mCannon.get(cannon);
                c.launches = data.launches;
                c.delay = data.delay;
                c.reload = data.reload;
                return cannon;
            case LAUNCHER:

                //return weapon;
            case BEAM:

                //return weapon;
            default:
                throw new IllegalArgumentException(data.type + " is not a known type");

        }
    }

    private static void buildTurret(int entityID, WeaponData data, int ship, Mount.Weapon mount){
        Mounted m = mMounted.get(entityID);
        m.parent = ship;
        m.location = mount.location;
        m.theta = mount.angle;

        MountedSprite s = mSprite.get(entityID);
        s.name = Services.WEAPON_IMG_PATH + data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.ACTOR;

        Turret t = mTurret.get(entityID);
        t.barrels = data.barrels;
        t.omegaMax = data.rotationSpeed;t.setArcs(mount.angle, mount.arc);
    }

    private static class Wrapper{
        String name;
        WeaponData data;
    }

    private static class WeaponData{
        // Generic to all weapons
        WeaponType type;
        String imgName;
        Vector2 imgCenter;
        float rotationSpeed;
        Array<Vector2> barrels;

        // Specific to Cannons and Launchers
        String launches;
        float delay, reload;

        // Specific to Beams
        String beamImgName;
        Vector2 beamImgCenter;
        float range;
    }

    private enum WeaponType{
        CANNON, LAUNCHER, BEAM
    }
}
