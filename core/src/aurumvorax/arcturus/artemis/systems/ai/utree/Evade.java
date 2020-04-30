package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Node;
import aurumvorax.arcturus.artemis.components.AIData;

public class Evade extends Node{

    @Override
    protected float evaluate(AIData bb){
        return 1;
    }

    @Override
    public void execute(AIData bb){
    //    System.out.println("Threat!");
    }
}
