package aurumvorax.arcturus.artemis.systems.ai;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class Attack extends LeafTask<ShipAI>{


    @Override
    public Status execute(){
        ShipAI blackboard = getObject();
        Navigation.execute(blackboard.currentAI, Navigation.Seek(blackboard.currentAI, blackboard.activeTarget));
        return Status.RUNNING;
    }

    @Override
    protected Task<ShipAI> copyTo(Task<ShipAI> task){
        return null;
    }
}
