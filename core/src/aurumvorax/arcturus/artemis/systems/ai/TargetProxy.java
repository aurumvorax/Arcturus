package aurumvorax.arcturus.artemis.systems.ai;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class TargetProxy implements Location<Vector2>{

    private Vector2 position = new Vector2();
    private float orientation;

    @Override public Vector2 getPosition(){ return position; }
    @Override public float getOrientation(){ return orientation; }
    @Override public void setOrientation(float orientation){}
    @Override public float vectorToAngle(Vector2 vector){ return vector.angle(); }

    @Override public Vector2 angleToVector(Vector2 outVector, float angle){
        outVector.set(1,0);
        outVector.rotate(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation(){
        return null;
    }
}
