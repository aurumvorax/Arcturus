package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class MotionSystem extends IteratingSystem{

    ComponentMapper<Position> pm;
    ComponentMapper<Velocity> vm;

    public MotionSystem(){
        super(Aspect.all(Position.class, Velocity.class));
    }

    @Override
    protected void process(int entityId){
        Position p = pm.get(entityId);
        Velocity v = vm.get(entityId);

        p.position.mulAdd(v.velocity, world.delta);
        p.theta += v.omega * world.delta;
    }
}
