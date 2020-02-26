package aurumvorax.arcturus.aiUtree;

import aurumvorax.arcturus.artemis.components.AIData;
import com.badlogic.gdx.utils.Array;

public abstract class Group extends Node{

    private Array<Node> children = new Array<>(4);


    void addChild(Node node){ children.add(node); }

    @Override
    public void execute(AIData bb){}  // Not used by Groups

    @Override
    protected void select(AIData bb){
        float bestScore = 0;
        Node bestChild = null;

        for(Node child : children){
            float score = child.evaluate(bb);
            if(score > bestScore){
                bestScore = score;
                bestChild = child;
            }
        }

        if(bestScore != 0)
            bestChild.select(bb);
    }
}
