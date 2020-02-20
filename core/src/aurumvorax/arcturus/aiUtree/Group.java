package aurumvorax.arcturus.aiUtree;

import com.badlogic.gdx.utils.Array;

public abstract class Group extends Node<Blackboard>{

    private Array<Node<Blackboard>> children = new Array<>(4);


    void addChild(Node<Blackboard> node){ children.add(node); }

    @Override
    protected void execute(Blackboard bb){
        float bestScore = 0;
        Node<Blackboard> bestChild = null;

        for(Node<Blackboard> child : children){
            float score = child.evaluate(bb);
            if(score > bestScore){
                bestScore = score;
                bestChild = child;
            }
        }

        if(bestScore != 0)
            bestChild.execute(bb);
    }
}
