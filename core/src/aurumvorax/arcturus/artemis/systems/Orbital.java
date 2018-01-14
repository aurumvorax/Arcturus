package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Orbit;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.*;

public class Orbital extends IteratingSystem{

    private Vector2 temp = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Orbit> mOrbit;

    public Orbital(){
        super(Aspect.all(Physics2D.class, Orbit.class));
    }

    @Override
    protected void process(int entityId){
        Physics2D p = mPhysics.get(entityId);
        Orbit o = mOrbit.get(entityId);
        float distance = p.p.dst(mPhysics.get(o.parent).p);
        o.time += (world.getDelta() * (o.sweep / distance));
        o.time = o.time % Utils.TWOPI;
        temp.set(o.major * (float)cos(o.time), o.minor * (float)sin(o.time));
        temp.rotate(o.tilt);
        p.p.set(o.center).add(temp).add(mPhysics.get(o.parent).p);
    }
}

