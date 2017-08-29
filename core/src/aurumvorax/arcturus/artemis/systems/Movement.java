package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class Movement extends IteratingSystem{

    private ComponentMapper<Physics2D> pm;

    public Movement(){
        super(Aspect.all(Physics2D.class));
    }

    @Override
    protected void process(int entityId){
        Physics2D physics2D = pm.get(entityId);
        float  delta = world.delta;
        physics2D.p.mulAdd(physics2D.v, delta);
        physics2D.theta += physics2D.omega * delta;
    }
}
