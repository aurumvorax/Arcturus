package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Ship;
import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;

public class MasterAI extends IteratingSystem{

    private SensorsAI sensorsAI = new SensorsAI();
    private GunneryAI gunneryAI = new GunneryAI();
    private TargetingAI targetingAI = new TargetingAI();


    public MasterAI(){
        super(Aspect.all(Ship.class));
    }

    @Override
    public void initialize(){
        world.inject(sensorsAI);
        world.inject(targetingAI);
        world.inject(gunneryAI);
    }

    @Override
    protected void process(int entityId){
        sensorsAI.process(entityId);
        targetingAI.process(entityId);
        gunneryAI.process(entityId);
    }
}
