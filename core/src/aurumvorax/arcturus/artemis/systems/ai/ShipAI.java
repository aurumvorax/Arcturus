package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.AIShip;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.components.shipComponents.PoweredMotion;
import aurumvorax.arcturus.artemis.systems.ai.steering.*;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ai.btree.BehaviorTree;

public class ShipAI extends IteratingSystem{

    private BehaviorTree<ShipAI> btree;
    int currentAI;
    int activeTarget;

    private static ComponentMapper<PlayerShip> mPlayer;

    @SuppressWarnings("unchecked")
    public ShipAI(){
        super(Aspect.all(Physics2D.class, PoweredMotion.class).one(AIShip.class, PlayerShip.class));
    }

    @Override
    public void initialize(){
        BuildBehaviourTree();
        world.inject(AvoidCollision.INSTANCE);
        world.inject(Seek.INSTANCE);
        world.inject(Stop.INSTANCE);
        world.inject(Steer.INSTANCE);
        world.inject(Arrive.INSTANCE);
        world.inject(Separation.INSTANCE);
    }

    @Override
    protected void process(int entityId){
        currentAI = entityId;
        if(!mPlayer.has(entityId))
            btree.step();
    }

    private void BuildBehaviourTree(){
        btree = new BehaviorTree<ShipAI>(new Attack(), this);
    }

}
