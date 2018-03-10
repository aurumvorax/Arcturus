package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Celestial;
import aurumvorax.arcturus.artemis.components.Orbit;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.SimpleSprite;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public enum TerrainFactory{
    INSTANCE;

    private static World world;
    private static HashMap<String, TerrainData> terrains;
    private static Archetype protoStar;
    private static Archetype protoOrbital;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Orbit> mOrbit;

    public static void init(World world){
        TerrainFactory.world = world;
        world.inject(INSTANCE);

        terrains = new HashMap<>();
        protoStar = new ArchetypeBuilder()
                .add(Celestial.class)
                .add(Physics2D.class)
                .add(SimpleSprite.class)
                .build(world);

        protoOrbital = new ArchetypeBuilder(protoStar)
                .add(Orbit.class)
                .build(world);

        terrains = new HashMap<>();
        for(FileHandle entry : Services.TERRAIN_PATH.list()){
            Wrapper wrapper = Services.json.fromJson(Wrapper.class, entry);
            terrains.put(wrapper.name, wrapper.data);
            Gdx.app.debug("INIT", "Registered Celestial Body - " + wrapper.name);
        }
    }

    public static int createStar(String type, float x, float y){

        if(!terrains.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);

        TerrainData data = terrains.get(type);
        int star = world.create(protoStar);
        buildTerrain(star, data, x, y);
        return star;
    }

    private static void buildTerrain(int terrain, TerrainData data, float x, float y){

        Physics2D p = mPhysics.get(terrain);
        p.p.set(x, y);

        SimpleSprite s = mSprite.get(terrain);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;
    }

    public static int createOrbital(String type, int parent){


        if(!terrains.containsKey(type))
            throw new IllegalArgumentException("Invalid projectile type - " + type);

        TerrainData data = terrains.get(type);
        int orbital = world.create(protoOrbital);


        float semiminor = data.semimajor * (float)Math.sqrt(1 - Math.pow(data.eccentricity, 2));
        Orbit o = mOrbit.get(orbital);
        o.parent = parent;
        o.major = data.semimajor * 2;
        o.minor = semiminor * 2;
        o.tilt = data.tilt;
        o.time = data.offset;
        o.sweep = data.sweep;
        o.center.set(data.semimajor * data.eccentricity, 0).rotate(o.tilt);

        buildTerrain(orbital, data, data.semimajor, semiminor);
        return orbital;
    }


    private static class Wrapper{
        String name;
        TerrainData data;
    }

    private static class TerrainData{

        //Common to all projectile types
        String imgName;
        Vector2 imgCenter;

        // Orbital parameters

        float semimajor;
        float eccentricity;
        float offset;
        float tilt;
        double sweep;
    }
}
