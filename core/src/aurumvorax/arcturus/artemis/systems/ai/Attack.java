package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.systems.ai.steering.AvoidCollision;
import aurumvorax.arcturus.artemis.systems.ai.steering.Seek;
import aurumvorax.arcturus.artemis.systems.ai.steering.Steer;
import aurumvorax.arcturus.artemis.systems.ai.steering.Stop;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Attack extends LeafTask<ShipAI>{


    @Override
    public Status execute(){
        ShipAI blackboard = getObject();

        Steer.execute(blackboard.currentAI, AvoidCollision.calc(blackboard.currentAI), 0);
        return Status.RUNNING;
    }

    @Override
    protected Task<ShipAI> copyTo(Task<ShipAI> task){
        return null;
    }
}
