package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Node;
import aurumvorax.arcturus.artemis.components.AIData;

public class DoOnce extends Node{

    private boolean first = true;

    @Override
    protected float evaluate(AIData bb){
        if(first){
            first = false;
            return 3;
        }

        return 0;
    }

    @Override
    public void execute(AIData bb){
        if(first)
            System.out.println("FIRST");
    }
        // run the long term planner

}
