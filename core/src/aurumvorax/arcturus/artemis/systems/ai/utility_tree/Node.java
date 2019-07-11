package aurumvorax.arcturus.artemis.systems.ai.utility_tree;

import aurumvorax.arcturus.artemis.components.AIData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;

public abstract class Node{

    UtilityTree root;
    Node parent;
    IntArray inputIDs = new IntArray(true, 5);
    FloatArray weights = new FloatArray(true, 5);
    Array<Node> children = new Array<>(2);


    protected abstract void action(AIData bb);

    void tick(AIData bb){
        action(bb);

        if((children != null) && (children.size > 0)){
            Node best = null;
            float bestScore = -1;

            for(Node child : children){
                float score = child.calcScore(bb);
                if(score > bestScore){
                    bestScore = score;
                    best = child;
                }
            }

            if(best != null)
                best.tick(bb);
        }
    }

    private float calcScore(AIData bb){
        float tally = 1;

        for(int i = 0; i < inputIDs.size; i++){
            float data = root.getInputData(inputIDs.get(i), bb);
            tally *= (weights.get(i) >= 0) ? data * weights.get(i) : -weights.get(i) * (1 - data) ;
        }

        return tally;
    }
}
