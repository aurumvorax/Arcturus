package aurumvorax.arcturus.artemis.systems.ai.pilot;

import aurumvorax.arcturus.artemis.components.PoweredMotion;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;

public class Steer{

    private static Vector2 steer = new Vector2();

    private static ComponentMapper<PoweredMotion> mPowered;


    public Steer(World world){
        world.inject(this);
        world.inject(new Face());
        world.inject(new Stop());
        world.inject(new Arrive());
        world.inject(new MaintainDistance());
    }

    public static void execute(int owner, Vector2 course, float helm){
        PoweredMotion pm = mPowered.get(owner);
        if(course == null)
            return;
        pm.accel.set(course);
        pm.alpha = helm;
    }

    public static Vector2 blend(Vector2 course1, float weight1, Vector2 course2, float weight2){
        if(course1 == null)
            steer.setZero();
        else
            steer.set(course1).scl(weight1);
        if(course2 != null)
            steer.mulAdd(course2, weight2);
        return steer;
    }

    public static Vector2 priority(Vector2 course1, Vector2 course2){
        if(course1 != null && !course1.isZero())
            return steer.set(course1);
        if(course2 != null)
            return steer.set(course2);
        return steer.setZero();
    }
}
