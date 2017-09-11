package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.artemis.components.Ephemeral;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class EphemeralDecay extends IteratingSystem{

    private static ComponentMapper<Ephemeral> mEphemeral;

    public EphemeralDecay(){
        super(Aspect.all(Ephemeral.class));
    }

    @Override
    protected void process(int entityID){
        mEphemeral.get(entityID).lifespan -= world.delta;
        if(mEphemeral.get(entityID).lifespan < 0)
            world.delete(entityID);
    }
}
