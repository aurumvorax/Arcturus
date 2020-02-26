package aurumvorax.arcturus.aiUtree;

import aurumvorax.arcturus.artemis.components.AIData;

public abstract class Node{

    protected abstract float evaluate(AIData bb);
    public abstract void execute(AIData bb);

    protected void select(AIData bb){
        bb.setCurrent(this);
    }

}
