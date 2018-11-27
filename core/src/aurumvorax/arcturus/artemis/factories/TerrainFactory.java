package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;


public class TerrainFactory{
    private static final TerrainFactory INSTANCE = new TerrainFactory();

    private static World world;
    private static Archetype protoStar;
    private static Archetype protoOrbital;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Celestial> mCelestial;
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

    static int createStar(String name, String imgName, Vector2 imgCenter){
        int star = world.create(protoStar);
        mCelestial.get(star).name = name;
        SimpleSprite s = mSprite.get(star);
        s.name = imgName;
        s.offsetX = imgCenter.x;
        s.offsetY = imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;
        return star;
    }

    static int createOrbital(SystemData.OrbitalData data, int parent){
        int orbital = world.create(protoOrbital);
        mCelestial.get(orbital).name = data.name;
        SimpleSprite s = mSprite.get(orbital);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;
        float semiminor = data.semimajor * (float)Math.sqrt(1 - Math.pow(data.eccentricity, 2));
        Orbit o = mOrbit.get(orbital);
        o.parent = parent;
        o.major = data.semimajor * 2;
        o.minor = semiminor * 2;
        o.tilt = data.tilt;
        o.time = data.offset;
        o.sweep = data.sweep;
        o.center.set(data.semimajor * data.eccentricity, 0).rotate(o.tilt);
        mPhysics.get(orbital).p.set(data.semimajor, 0).rotate(o.tilt).add(o.center);

        if(data.dock){
            mDockable.create(orbital);
        }

        return orbital;
    }
}
