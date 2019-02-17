package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import com.artemis.*;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Collision extends BaseEntitySystem{

    private static Bag<CollisionPair> collisionPairs = new Bag<>();
    private static Manifold manifold = new Manifold();

    private static CollisionHandler notify = new NullHandler();
    private static CollisionHandler crash = new BounceHandler();
    private static CollisionHandler boom = new BoomHandler();
    private static CollisionHandler pewpew = new BeamHandler();

    private static ComponentMapper<CollisionPolygon> mPolygon;
    private static ComponentMapper<Beam> mBeam;

    public Collision(){
        super(Aspect.all(CollisionRadius.class, Physics2D.class));
    }

    @Override
    public void initialize(){
        world.inject(new TestBroadPhase());
        world.inject(new TestCC());
        world.inject(new TestCP());
        world.inject(new TestPP());
        world.inject(new TestBeam());
        world.inject(new TestPoint());
        world.inject(notify);
        world.inject(crash);
        world.inject(boom);
        world.inject(pewpew);

        EntitySubscription actors = world.getAspectSubscriptionManager().get(Aspect.all(
                Ship.class,
                Physics2D.class,
                CollisionRadius.class));

        EntitySubscription bullets = world.getAspectSubscriptionManager().get(Aspect.all(
                Projectile.class,
                Physics2D.class,
                CollisionRadius.class));

        EntitySubscription beams = world.getAspectSubscriptionManager().get(Aspect.all(
                Beam.class,
                Mounted.class));

        collisionPairs.add(new CollisionPair(actors, actors, crash));
        collisionPairs.add(new CollisionPair(actors, bullets, boom));
        collisionPairs.add(new CollisionPair(actors, beams, pewpew));
    }

    @Override
    protected void processSystem(){
        ((BeamHandler)pewpew).setDelta(world.getDelta());   // need world.delta for dps calculations

        for(CollisionPair collisionPair : collisionPairs)
            collisionPair.runPair();
    }

    private class CollisionPair{

        private EntitySubscription group1;
        private EntitySubscription group2;
        private CollisionHandler handler;

        CollisionPair(EntitySubscription group1, EntitySubscription group2, CollisionHandler handler){
            this.group1 = group1;
            this.group2 = group2;
            this.handler = handler;
        }

        private void runPair(){
            if(group1 == group2){
                IntBag list1 = group1.getEntities();
                for(int i = 0; i < list1.size(); i++){
                    for(int j = i; j < list1.size(); j++){
                        if(!TestBroadPhase.test(list1.get(i), list1.get(j)))
                            continue;
                        checkCollision(list1.get(i), list1.get(j));
                        if(manifold.contacts != 0)
                            handler.onCollide(list1.get(i), list1.get(j), manifold);
                    }
                }
            }else{
                IntBag list1 = group1.getEntities();
                IntBag list2 = group2.getEntities();
                for(int i = 0; i < list1.size(); i++){
                    for(int j = 0; j < list2.size(); j++){
                        if(!TestBroadPhase.test(list1.get(i), list2.get(j)))
                            continue;
                        checkCollision(list1.get(i), list2.get(j));
                        if(manifold.contacts != 0)
                            handler.onCollide(list1.get(i), list2.get(j), manifold);
                    }
                }
            }
        }

        private void checkCollision(int entityA, int entityB){
            manifold.reset();

            if(mBeam.has(entityB))
                TestBeam.test(entityA, entityB, manifold);
            else if(mBeam.has(entityA))
                TestBeam.test(entityB, entityA, manifold);

            else if(!mPolygon.has(entityA) && !mPolygon.has(entityB))    // Circle - Circle
                TestCC.test(entityA, entityB, manifold);

            else if(!mPolygon.has(entityA) && mPolygon.has(entityB))    // Circle - Polygon
                TestCP.test(entityA, entityB, manifold);

            else if(mPolygon.has(entityA) && !mPolygon.has(entityB)){   // Polygon - Circle
                TestCP.test(entityB, entityA, manifold);
                manifold.normal.scl(-1.0f);
            }
            else if(mPolygon.has(entityA) && mPolygon.has(entityB))       // Polygon - Polygon
                TestPP.test(entityA, entityB, manifold);
        }
    }

    static class Manifold{
        Array<Vector2> contactPoints = new Array<>();
        Vector2 normal = new Vector2();
        int contacts;
        float[] penetration = new float[2];

        void reset(){
            contactPoints.clear();
            contacts = 0;
        }
    }
}

// Shield check for actors

// Actor - Actor - Newtonian + Damage(impact)
// Actor - Missile - Detonation + Damage(projectile)
// Actor - Projectile - Detonation + Damage(projectile)
// Actor - Beam - Beam terminus + Damage(beam)
// Missile - Projectile - detonation + damage(projectile)
// Missile - Beam - beam terminus + damage(beam)

//Resolvers needed
// Newtonian + impact damage = Crash(actor, actor)
// projectile detonation + damage = Boom(actor/missile, missile/bullet)
// beam impact - damage = PewPew(actor/missile, beam)
