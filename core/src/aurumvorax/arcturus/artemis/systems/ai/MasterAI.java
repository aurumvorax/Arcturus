package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.aiUtree.UtilityTree;
import aurumvorax.arcturus.artemis.components.Ship;
import aurumvorax.arcturus.artemis.systems.ai.utree.Combat;
import aurumvorax.arcturus.artemis.systems.ai.utree.First;
import aurumvorax.arcturus.artemis.systems.ai.utree.Goals;
import aurumvorax.arcturus.artemis.systems.ai.utree.Threat;
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

    private UtilityTree buildUTree(){
        return new UtilityTree.Builder()
                .addGroup(new Combat())
                .addGroup(new First())
                .addGroup(new Threat())
                .addGroup(new Goals())
                .build();
    }
}
