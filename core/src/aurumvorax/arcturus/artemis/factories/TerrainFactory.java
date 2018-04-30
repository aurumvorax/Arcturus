package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.*;


public class TerrainFactory{
    private static final TerrainFactory INSTANCE = new TerrainFactory();

    private static World world;
    private static Archetype protoStar;
    private static Archetype protoOrbital;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Orbit> mOrbit;
    private static ComponentMapper<Dockable> mDockable;


    public static void init(World world){
        TerrainFactory.world = world;
        world.inject(INSTANCE);

        protoStar = new ArchetypeBuilder()
                .add(Celestial.class)
                .add(Physics2D.class)
                .add(SimpleSprite.class)
                .build(world);

        protoOrbital = new ArchetypeBuilder(protoStar)
                .add(Orbit.class)
                .build(world);
    }

    public static int createStar(String type, float x, float y){
        TerrainData data = EntityData.getTerrainData(type);
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
        TerrainData data = EntityData.getTerrainData(type);
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

        if(data.dockName != null)
            mDockable.create(orbital).name = data.dockName;

        buildTerrain(orbital, data, data.semimajor, semiminor);
        return orbital;
    }
}
