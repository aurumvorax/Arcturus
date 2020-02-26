package aurumvorax.arcturus.aiUtree;


import aurumvorax.arcturus.artemis.components.AIData;
import com.artemis.World;

import java.util.ArrayDeque;
import java.util.Deque;

public class UtilityTree extends Group{

    private UtilityTree(){} // Enforce use of Builder

    public void process(AIData bb){ select(bb); }
    @Override protected float evaluate(AIData bb){ return 0; } // Not used by root


    public static class Builder{

        UtilityTree root;
        Deque<Group> path = new ArrayDeque<>();
        World world;


        public Builder(World world){
            root = new UtilityTree();
            path.push(root);
            this.world = world;
        }

        public Builder addNode(Node node){
            world.inject(node);
            path.peek().addChild(node);
            return this;
        }

        public Builder addGroup(Group group){
            world.inject(group);
            path.peek().addChild(group);
            path.push(group);
            return this;
        }

        public Builder buildGroup(){
            if(path.peek() == root)
                throw new IllegalArgumentException("Cannot build the root group, use build() to finalise the tree");

            path.pop();
            return this;
        }

        public UtilityTree build(){
            return root;
        }
    }

}
