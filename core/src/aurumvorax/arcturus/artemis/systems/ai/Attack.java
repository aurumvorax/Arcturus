package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.systems.ai.steering.*;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Attack extends LeafTask<ShipAI>{


    @Override
    public Status execute(){
        ShipAI blackboard = getObject();

        Steer.execute(blackboard.currentAI, Arrive.calc(blackboard.currentAI, blackboard.activeTarget, 200), Stop.face(blackboard.currentAI));
        return Status.RUNNING;
    }

    @Override
    protected Task<ShipAI> copyTo(Task<ShipAI> task){
        return null;
    }
}
