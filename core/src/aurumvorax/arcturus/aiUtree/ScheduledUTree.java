package aurumvorax.arcturus.aiUtree;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.systems.ai.Scheduled;
import aurumvorax.arcturus.artemis.systems.ai.utree.*;
import com.artemis.ComponentMapper;
import com.artemis.World;

public class ScheduledUTree implements Scheduled{

    private static UtilityTree utree;

    private static ComponentMapper<AIData> mData;


    @Override
    public void init(World world){
        world.inject(this);

        utree =  new UtilityTree.Builder(world)
                    .addGroup(new Danger())             // Although no specific order is required, the AI
                        .addNode(new Fight())           // tree is built in priority order for the sake of clarity
                        .buildGroup()
                    .addNode(new DoOnce())
                    .addGroup(new Threat())
                        .addNode(new Evade())
                        .buildGroup()
                    .addNode(new Goals())
                    .build();
    }

    @Override
    public void runTask(int entityId){
        utree.process(mData.get(entityId));
    }
}
