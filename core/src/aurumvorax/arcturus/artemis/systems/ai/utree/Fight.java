package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Node;
import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.systems.ai.pilot.Face;
import aurumvorax.arcturus.artemis.systems.ai.pilot.MaintainDistance;
import aurumvorax.arcturus.artemis.systems.ai.pilot.Steer;

public class Fight extends Node{

    @Override
    protected float evaluate(AIData bb){
        return 1;
    }

    @Override
    public void execute(AIData bb){
        Steer.execute(bb.selfID, MaintainDistance.calc(bb.selfID, bb.targetID, 200f), Face.target(bb.selfID, bb.targetID));
    }
}
