package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Ship;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Selection extends BaseSystem{

    private static EntitySubscription docks;
    private static EntitySubscription ships;
    private static EntitySubscription missiles;
    private static EntitySubscription celestials;

    private static IntBag entities = new IntBag();
    private static IntBag selections = new IntBag();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<CollisionPolygon> mPoly;


    @Override
    public void initialize(){
        celestials = world.getAspectSubscriptionManager().get(Aspect.all(
                Celestial.class,
                Physics2D.class));

        docks = world.getAspectSubscriptionManager().get(Aspect.all(
                Dockable.class,
                Physics2D.class,
                CollisionRadius.class));

        missiles = world.getAspectSubscriptionManager().get(Aspect.all(
                Missile.class,
                Physics2D.class,
                CollisionRadius.class));

        ships = world.getAspectSubscriptionManager().get(Aspect.all(
                Ship.class,
                Physics2D.class,
                CollisionRadius.class));
    }

    @Override protected void processSystem(){}

    public static int getClosest(Vector2 center, entityType t){
        entities = t.getEntities();

        int best = -1;
        float bestDistance = Float.MAX_VALUE;

        for(int i = 0; i < entities.size(); i++){
            Vector2 target = mPhysics.get(entities.get(i)).p;
            float d2 = center.dst2(target);
            if(d2 < bestDistance){
                best = entities.get(i);
                bestDistance = d2;
            }
        }
        return best;
    }

    public static int getDock(){
        entities = entityType.DOCK.getEntities();

        int best = -1;
        float bestDistance = Float.MAX_VALUE;
        Vector2 center = mPhysics.get(PlayerShip.getID()).p;

        for(int i = 0; i < entities.size(); i++){
            Vector2 target = mPhysics.get(entities.get(i)).p;
            float d2 = center.dst2(target);
            if(d2 < bestDistance){
                best = entities.get(i);
                bestDistance = d2;
            }
        }
        if(best == -1){
            // TODO message - nowhere to dock in this system
            return -1;
        }
        if(TestBroadPhase.test(PlayerShip.getID(), best))
            return best;
        // TODO message - no dock within range
        return -1;
    }

    public static IntBag getWithin(Vector2 center, float range, entityType t){
        entities = t.getEntities();
        selections.clear();

        float r2 = range * range;

        for(int i = 0; i < entities.size(); i++){
            Vector2 target = mPhysics.get(entities.get(i)).p;
            float d2 = center.dst2(target);
            if(d2 < r2){
                selections.add(entities.get(i));
            }
        }
        return selections;
    }

    public static IntBag getAll(entityType t){
        return t.getEntities();
    }

    public static int getSelected(Vector2 click){
        entityType.ALL.getEntities();  // loads all entities

        for(int i = 0; i < entities.size(); i++){
            if(TestPoint.testC(entities.get(i), click))
                selections.add(entities.get(i));
        }

        if(selections.size() == 0)
            return -1;
        if(selections.size() == 1)
            return selections.get(0);

        return TestPoint.testPolys(click, selections);
    }

    public enum entityType{
        DOCK{
            public IntBag getEntities(){
                return docks.getEntities();
            }},

        SHIP{
            public IntBag getEntities(){
                return ships.getEntities();
            }},

        MISSILE{
            public IntBag getEntities(){
                return missiles.getEntities();
            }},

        CELESTIAL{
            public IntBag getEntities(){
                return celestials.getEntities();
            }},

        ALL{
            public IntBag getEntities(){
                entities.clear();
                entities.addAll(celestials.getEntities());
                entities.addAll(docks.getEntities());
                entities.addAll(missiles.getEntities());
                entities.addAll(ships.getEntities());
                return entities;
            }};

        public abstract IntBag getEntities();
    }


}
