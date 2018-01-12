package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Celestial;
import aurumvorax.arcturus.artemis.components.Orbit;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.SimpleSprite;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public enum TerrainFactory{
    INSTANCE;

    private static World world;
    private static HashMap<String, CelestialData> celestials;
    private static Archetype protoStar;
    private static Archetype protoOrbital;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Orbit> mOrbit;

    public static void init(World world){
        TerrainFactory.world = world;
        world.inject(INSTANCE);

        celestials = new HashMap<>();
        protoStar = new ArchetypeBuilder()
                .add(Celestial.class)
                .add(Physics2D.class)
                .add(SimpleSprite.class)
                .build(world);

        protoOrbital = new ArchetypeBuilder(protoStar)
                .add(Orbit.class)
                .build(world);

        celestials = new HashMap<>();
        for(FileHandle entry : Services.TERRAIN_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            celestials.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Celestial Body - " + wrapper.name);
        }
    }

    public static int createStar(String type, float x, float y, float t){
        if(!celestials.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);

        CelestialData data = celestials.get(type);
        int star = world.create(protoStar);

        Physics2D p = mPhysics.get(star);
        p.p.set(x, y);
        p.theta = t;

        SimpleSprite s = mSprite.get(star);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;

        return star;
    }

    private static class Wrapper{
        String name;
        CelestialData data;
    }

    private static class CelestialData{

        //Common to all projectile types
        String imgName;
        Vector2 imgCenter;

        // Orbital parameters

    }
}
