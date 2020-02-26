package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.aiUtree.UtilityTree;
import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Sensors;
import aurumvorax.arcturus.artemis.components.Ship;
import aurumvorax.arcturus.artemis.systems.ai.utree.Evade;
import aurumvorax.arcturus.artemis.systems.ai.utree.First;
import aurumvorax.arcturus.artemis.systems.ai.utree.Goals;
import aurumvorax.arcturus.artemis.systems.ai.utree.Threat;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class MasterAI extends IteratingSystem{

    private Detection detection = new Detection();
    private UtilityTree utilityTree;

    private static ComponentMapper<AIData> mAIData;


    public MasterAI(){
        super(Aspect.all(Ship.class, AIData.class, Sensors.class).exclude(Player.class));

    }

    @Override
    public void initialize(){
        detection.init(world);
        utilityTree = buildUTree();
    }

    @SuppressWarnings("unchecked")
    // This is for executing the currentAction stored in AIData. It's perfectly safe, as utilityTree(and therefore
    // it's superclass, Node, is instantiated with AIData in this very class.
    @Override
    protected void process(int entityId){
        AIData data = mAIData.get(entityId);

        // scheduled
            detection.process(entityId);
            utilityTree.process(data);

        //always
            if(data.currentAction != null)
                data.currentAction.execute(data);
    }

    private UtilityTree buildUTree(){
        return new UtilityTree.Builder(getWorld())
                .addGroup(new Threat())
                    .addNode(new Evade())
                    .buildGroup()
                .addNode(new First())
                .addNode(new Goals())
                .build();
    }
}
