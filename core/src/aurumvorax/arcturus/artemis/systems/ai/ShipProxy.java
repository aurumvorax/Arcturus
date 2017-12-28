package aurumvorax.arcturus.artemis.systems.ai;


import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.math.Vector2;

public class ShipProxy extends SteerableAdapter<Vector2>{

    private int shipID;
    private boolean tagged;

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<PoweredMotion> mPowered;

    public ShipProxy(int shipID){ this.shipID = shipID; }

    public int getShipID(){ return shipID; }
    @Override public Vector2 getLinearVelocity(){ return mPhysics.get(shipID).v; }
    @Override public float getAngularVelocity(){ return mPhysics.get(shipID).omega; }
    @Override public float getBoundingRadius(){ return mRadius.get(shipID).radius; }
    @Override public boolean isTagged(){ return tagged; }
    @Override public void setTagged(boolean tagged){ this.tagged = tagged; }
    @Override public float getMaxLinearSpeed(){ return mPowered.get(shipID).maxV; }
    @Override public float getMaxLinearAcceleration(){ return mPowered.get(shipID).thrust; }
    @Override public float getMaxAngularSpeed(){ return mPowered.get(shipID).maxO; }
    @Override public float getMaxAngularAcceleration(){ return mPowered.get(shipID).rotation; }
    @Override public Vector2 getPosition(){ return mPhysics.get(shipID).p; }
    @Override public float getOrientation(){ return mPhysics.get(shipID).theta; }
}
