package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.UtilityTree;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.inputs.IsPlayer;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.inputs.MyHealth;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes.Attack;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes.DoNothing;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes.Flee;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class ShipAI extends IteratingSystem{

    private UtilityTree tree;

    private static ComponentMapper<AIData> mAI;


    public ShipAI(){
        super(Aspect.all(Physics2D.class, PoweredMotion.class, AIData.class));
    }

    @Override
    public void initialize(){
       // tree = buildTree();
    }

    @Override
    protected void process(int entityID){
       // mAI.get(entityID).selfID = entityID;
       // tree.tick(mAI.get(entityID));
    }

    private UtilityTree buildTree(){

        return new UtilityTree.Builder(getWorld())
                .addInput("Health", new MyHealth())
                .addInput("Player", new IsPlayer())

                .addNode(new Flee())
                    .linkInput("Health", -0.5f)
                    .linkInput("Player", -1f)
                    .end()
                .addNode(new Attack())
                    .linkInput("Health", 0.5f)
                    .linkInput("Player", -1f)
                    .end()
                .addNode(new DoNothing())
                    .linkInput("Player", 1f)
                    .end()

                .build();
    }
}
