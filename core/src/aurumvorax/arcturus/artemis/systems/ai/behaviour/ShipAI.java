package aurumvorax.arcturus.artemis.systems.ai.behaviour;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.ai.gunnery.GunneryAI;
import aurumvorax.arcturus.artemis.systems.ai.steering.Steer;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.btree.BehaviorTree;

public class ShipAI extends IteratingSystem{

    private BehaviorTree<ShipAI> btree;
    int currentAI;
    int activeTarget;

    private static ComponentMapper<Player> mPlayer;

    @SuppressWarnings("unchecked")
    public ShipAI(){
        super(Aspect.all(Physics2D.class, PoweredMotion.class).one(AIData.class, Player.class));
    }

    @Override
    public void initialize(){
        BuildBehaviourTree();
        Steer.initialize(world);
        GunneryAI.initialize(world);
    }

    @Override
    protected void process(int entityId){
        currentAI = entityId;
        if(!mPlayer.has(entityId)){
            btree.step();
        }else
            activeTarget = entityId;
    }

    private void BuildBehaviourTree(){
        btree = new BehaviorTree<>(new Attack(), this);
    }

}
