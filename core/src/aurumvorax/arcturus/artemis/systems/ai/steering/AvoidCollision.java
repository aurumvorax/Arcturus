package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.Proximity;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class AvoidCollision implements Proximity.Callback{

    private static AvoidCollision INSTANCE = new AvoidCollision();
    private AvoidCollision(){} // Single static class with DI/callback

    private static Vector2 collision = new Vector2();
    private static Vector2 deltaP = new Vector2();
    private static Vector2 deltaV = new Vector2();
    private static Vector2 firstDeltaP = new Vector2();
    private static Vector2 firstDeltaV = new Vector2();
    private static Vector2 closestPoint = new Vector2();
    private static int owner;
    private static int firstContact;
    private static float shortestToCPA;
    private static float distanceCPA;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<PoweredMotion> mPowered;

    public static void initialize(World world){ world.inject(INSTANCE); }

    public static Vector2 calc(int owner){
        AvoidCollision.owner = owner;
        firstContact = -1;
        shortestToCPA = Float.POSITIVE_INFINITY;
        distanceCPA = 0;

        int contacts = Proximity.findContacts(owner, 300, INSTANCE);

        if((contacts == 0) || (firstContact == -1))     // No collisions to avoid
            return collision.setZero();

        Physics2D ownerP = mPhysics.get(owner);
        Physics2D contactP = mPhysics.get(firstContact);
        float radii = mRadius.get(owner).radius + mRadius.get(firstContact).radius;

            // If we are heading straight on, or currently colliding, avoid current position
        if((distanceCPA <= 0) || (ownerP.p.dst2(contactP.p) < (radii * radii))){
            deltaP.set(contactP.p).sub(ownerP.p);
        }else{      // Otherwise, avoid CPA
            deltaP.set(firstDeltaP).mulAdd(firstDeltaV, shortestToCPA);
        }

        collision.set(deltaP).nor().scl(-mPowered.get(owner).thrust);
        return collision;
    }

    @Override
    public boolean reportContact(int contact){

        Physics2D ownerPhysics = mPhysics.get(owner);
        Physics2D contactPhysics = mPhysics.get(contact);
        deltaP.set(contactPhysics.p).sub(ownerPhysics.p);
        deltaV.set(contactPhysics.v).sub(ownerPhysics.v);
        float relativeSpeed2 = deltaV.len2();


        if(relativeSpeed2 == 0)     // No relative speed, no collision.
            return false;

        float timeToCPA = -deltaP.dot(deltaV) / relativeSpeed2;
        if(timeToCPA <= 0)    // Negative time to intercept means objects moving away from each other, no collision
            return false;

        if(timeToCPA >= shortestToCPA)    // Not the most immediate collision possibility, reject for now.
            return false;

        closestPoint.set(deltaP).mulAdd(deltaV, timeToCPA);
        float closestApproach = closestPoint.len();
        if(closestApproach > mRadius.get(owner).radius + mRadius.get(contact).radius) // Closest approach is a miss.
            return false;

        shortestToCPA = timeToCPA;      // At this point, we have a projected collision with our contact
        firstContact = contact;
        distanceCPA = closestApproach;
        firstDeltaP.set(deltaP);
        firstDeltaV.set(deltaV);
        return true;
    }
}
