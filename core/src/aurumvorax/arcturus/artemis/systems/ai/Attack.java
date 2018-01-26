package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.systems.ai.gunnery.GunneryAI;
import aurumvorax.arcturus.artemis.systems.ai.steering.*;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Attack extends LeafTask<ShipAI>{


    @Override
    public Status execute(){
        ShipAI blackboard = getObject();
        //Steer.execute(blackboard.currentAI, Steer.blend(Separation.calc(blackboard.currentAI, 5000000), 1, Arrive.calc(blackboard.currentAI, blackboard.activeTarget, 0), 1), 0);
        //Steer.execute(blackboard.currentAI, Steer.priority(AvoidCollision.calc(blackboard.currentAI), Arrive.calc(blackboard.currentAI, blackboard.activeTarget, 200)), 0);
        Steer.execute(blackboard.currentAI, Arrive.calc(blackboard.currentAI, blackboard.activeTarget, 200), Face.heading(blackboard.currentAI));
        GunneryAI.update(blackboard.currentAI,blackboard.activeTarget);
        return Status.RUNNING;
    }

    @Override protected Task<ShipAI> copyTo(Task<ShipAI> task){ return null; }
}
