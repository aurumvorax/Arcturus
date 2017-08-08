package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

@SuppressWarnings("WeakerAccess")
public class Movement extends IteratingSystem{

    ComponentMapper<Position> pm;
    ComponentMapper<Velocity> vm;

    public Movement(){
        super(Aspect.all(Position.class, Velocity.class));
    }

    @Override
    protected void process(int entityId){
        Position position = pm.get(entityId);
        Velocity velocity = vm.get(entityId);
        float  delta = world.delta;
        position.p.mulAdd(velocity.v, delta);
        position.theta += velocity.omega * delta;
    }
}
