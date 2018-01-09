package aurumvorax.arcturus.artemis.systems.ai.steering;

import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Steer{

    private static Vector2 steer = new Vector2();

    // Injected from ShipAI
    private static ComponentMapper<PoweredMotion> mPowered;

    public static void execute(int owner, Vector2 course, float helm){
        PoweredMotion pm = mPowered.get(owner);
        pm.accel.set(course);
        pm.alpha = helm;
    }

    public static Vector2 combine(Vector2 course1, float weight1, Vector2 course2, float weight2){
        steer.set(course1).scl(weight1);
        steer.mulAdd(course2, weight2);
        return steer;
    }
}
