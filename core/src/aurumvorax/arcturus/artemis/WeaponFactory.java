package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Mounted;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class WeaponFactory{

    private static ShipFactory INSTANCE = new ShipFactory();
    private static World world;
    private static HashMap<String, WeaponData> weapons;
    private static Archetype protoWeapon;


    public void init(World world){
        WeaponFactory.world = world;
        world.inject(INSTANCE);
        protoWeapon = new ArchetypeBuilder()
                .add(Mounted.class)
                .build(world);
        weapons = new HashMap<>();
        for(FileHandle entry : Services.WEAPON_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            weapons.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Ship - " + wrapper.name);
        }
    }

    public static int create(String type, int ship){
        if(!weapons.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);

        int weapon  = world.create(protoWeapon);
        WeaponData data = weapons.get(type);

        switch(data.type){
            case CANNON:

                return 0;
            case LAUNCHER:

                return 0;
            case BEAM:

                return 0;
            default:
                throw new IllegalArgumentException(data.type + " is not a known type");

        }
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
