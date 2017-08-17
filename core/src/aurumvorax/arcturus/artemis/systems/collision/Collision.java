package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Collidable;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.EntitySubscription;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Collision extends BaseEntitySystem{

    private Bag<CollisionPair> collisionPairs;
    private static Manifold manifold = new Manifold();

    private EntitySubscription ships;

    public Collision(){
        super(Aspect.all(Collidable.class, Position.class));
    }

    @Override
    public void initialize(){
        world.inject(BroadPhaseTest.INSTANCE);
        world.inject(CCTest.INSTANCE);
        world.inject(NullHandler.INSTANCE);

        ships = world.getAspectSubscriptionManager().get(Aspect.all(
                Position.class,
                Velocity.class,
                Collidable.class));

        collisionPairs = new Bag<>();
        collisionPairs.add(new CollisionPair(ships, ships, NullHandler.INSTANCE));
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

        void runPair(){
            if(group1 == group2){
                IntBag list1 = group1.getEntities();
                for(int i = 0; i < list1.size(); i++){
                    for(int j = i; j < list1.size(); j++){

                        if(BroadPhaseTest.test(list1.get(i), list1.get(j)))
                            // narrowphase test goes here
                        handler.onCollide(list1.get(i), list1.get(j), manifold);
                    }
                }
            }else{
                IntBag list1 = group1.getEntities();
                IntBag list2 = group2.getEntities();
                for(int i = 0; i < list1.size(); i++){
                    for(int j = 0; j < list2.size(); j++){

                        // Test for collision
                        handler.onCollide(list1.get(i), list2.get(j), manifold);
                    }
                }
            }
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
