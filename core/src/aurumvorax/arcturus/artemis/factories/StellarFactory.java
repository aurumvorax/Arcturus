package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.systems.render.Renderer;
import aurumvorax.arcturus.galaxy.StellarData;
import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;


public class StellarFactory{
    private static final StellarFactory INSTANCE = new StellarFactory();

    private static World world;
    private static Archetype protoStar;
    private static Archetype protoOrbital;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<SimpleSprite> mSprite;
    private static ComponentMapper<Celestial> mCelestial;
    private static ComponentMapper<Orbit> mOrbit;
    private static ComponentMapper<Dockable> mDockable;
    private static ComponentMapper<CollisionRadius> mRadius;


    public static void init(World world){
        StellarFactory.world = world;
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

    public static void buildTree(OrbitalData data, int parent){
        int thisOrbital = StellarFactory.createOrbital(data, parent);
        if(data.children != null)
            for(OrbitalData child : data.children)
                buildTree(child, thisOrbital);
    }

    public static int createStar(String name, String imgName, Vector2 imgCenter){
        int star = world.create(protoStar);
        mCelestial.get(star).name = name;
        SimpleSprite s = mSprite.get(star);
        s.name = imgName;
        s.offsetX = imgCenter.x;
        s.offsetY = imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;
        return star;
    }

    private static int createOrbital(OrbitalData data, int parent){
        int orbital = world.create(protoOrbital);
        mCelestial.get(orbital).name = data.name;
        SimpleSprite s = mSprite.get(orbital);
        s.name = data.imgName;
        s.offsetX = data.imgCenter.x;
        s.offsetY = data.imgCenter.y;
        s.layer = Renderer.Layer.PLANETARY;
        Orbit o = mOrbit.get(orbital);
        o.parent = parent;
        o.major = data.semimajor * 2;
        o.minor = data.semimajor * (float)Math.sqrt(1 - Math.pow(data.eccentricity, 2)) * 2;
        o.tilt = data.tilt;
        o.time = data.offset;
        o.sweep = data.sweep;
        o.center.set(data.semimajor * data.eccentricity, 0).rotate(o.tilt);
        mPhysics.get(orbital).p.set(data.semimajor, 0).rotate(o.tilt).add(o.center);

        if(data.dock){
            Dockable d = mDockable.create(orbital);
            d.name = data.name;
            CollisionRadius r = mRadius.create(orbital);
            r.radius = data.dockRadius;
        }

        return orbital;
    }

    @SuppressWarnings("unchecked")
    public static StellarData.Extra extractCurrentSystem(){
        StellarData.Extra currentSystem = new StellarData.Extra();

        IntBag ships = world.getAspectSubscriptionManager().get(Aspect.all(Ship.class).exclude(Player.class)).getEntities();
        int[] ids = ships.getData();
        for(int i = 0, s = ships.size(); s > i; i++){
            currentSystem.ships.add(ShipFactory.extract(ids[i]));
        }

        return currentSystem;
    }
}
