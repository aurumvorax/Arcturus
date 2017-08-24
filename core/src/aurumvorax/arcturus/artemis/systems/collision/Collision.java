package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.CollisionPolygon;
import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Collision extends BaseEntitySystem{

    private Bag<CollisionPair> collisionPairs = new Bag<>();
    private static Manifold manifold = new Manifold();

    private ComponentMapper<CollisionPolygon> mPolygon;

    public Collision(){
        super(Aspect.all(CollisionRadius.class, Position.class));
    }

    @Override
    public void initialize(){
        world.inject(new TestBroadPhase());
        world.inject(new TestCC());
        world.inject(new TestCP());
        world.inject(new TestPP());
        world.inject(NullHandler.INSTANCE);
        world.inject(BounceHandler.INSTANCE);

        EntitySubscription actors = world.getAspectSubscriptionManager().get(Aspect.all(
                Position.class,
                Velocity.class,
                CollisionRadius.class));

        collisionPairs.add(new CollisionPair(actors, actors, BounceHandler.INSTANCE));
    }

    @Override
    protected void processSystem(){
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
                        if(!TestBroadPhase.test(list1.get(i), list1.get(j)))
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

            if(!mPolygon.has(entityA) && !mPolygon.has(entityB))    // Circle - Circle
                TestCC.test(entityA, entityB, manifold);

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

// Actor - Actor - Newtonian + Damage(impact)
// Actor - Missile - Detonation + Damage(projectile)
// Actor - Bullet - Detonation + Damage(projectile)
// Actor - Beam - Beam terminus + Damage(beam)
// Missile - Bullet - detonation + damage(projectile)
// Missile - Beam - beam terminus + damage(beam)

//Resolvers needed
// Newtonian + impact damage = Crash(actor, actor)
// projectile detonation + damage = Boom(actor/missile, projectile)
// beam impact - damage = PewPew(actor/missile, beam)
