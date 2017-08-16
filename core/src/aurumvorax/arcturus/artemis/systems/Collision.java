package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Collidable;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import aurumvorax.arcturus.artemis.systems.collision.CollisionHandler;
import aurumvorax.arcturus.artemis.systems.collision.NullHandler;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.EntitySubscription;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;

public class Collision extends BaseEntitySystem{

    private Bag<CollisionPair> collisionPairs;

    EntitySubscription ships = world.getAspectSubscriptionManager().get(Aspect.all(
            Position.class,
            Velocity.class,
            Collidable.class));

    public Collision(){
        super(Aspect.all(Collidable.class, Position.class, Velocity.class));
    }


    @Override
    public void initialize(){
        collisionPairs = new Bag<>();
        collisionPairs.add(new CollisionPair(ships, ships, NullHandler.INSTANCE));
    }


    @Override
    protected void processSystem(){
        for(CollisionPair collisionPair : collisionPairs){

        }
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

                        // Test for collision
                        // Handle collision

                    }
                }

            }else{
                IntBag list1 = group1.getEntities();
                IntBag list2 = group2.getEntities();
                for(int i = 0; i < list1.size(); i++){
                    for(int j = 0; j < list2.size(); j++){

                        // Test for collision
                        // Handle collision


                    }
                }
            }

        }
    }
}
