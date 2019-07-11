package aurumvorax.arcturus.artemis.systems.ai.utility_tree;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.AIShip;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.inputs.MyHealth;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes.Attack;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes.Flee;
import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;

public class ShipAI extends IteratingSystem{

    private UtilityTree tree;

    @SuppressWarnings("unchecked")
    public ShipAI(){
        super(Aspect.all(Physics2D.class, PoweredMotion.class).one(AIShip.class, Player.class));
        tree = buildTree();
    }


    @Override
    protected void process(int entityId){

    }

    private UtilityTree buildTree(){

        return new UtilityTree.Builder(getWorld())
                .addInput("Health", new MyHealth())


                .addNode(new Flee())
                    .linkInput("Health", -0.5f)
                    .end()
                .addNode(new Attack())
                    .linkInput("Health", 0.5f)
                    .end()

                .build();
    }
}
