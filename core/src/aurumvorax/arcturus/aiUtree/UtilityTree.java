package aurumvorax.arcturus.aiUtree;


import java.util.ArrayDeque;
import java.util.Deque;

public class UtilityTree extends Group{


    private UtilityTree(){} // Enforce use of Builder

    public void tick(Blackboard bb){
        execute(bb);
    }

    @Override protected float evaluate(Blackboard bb){ return 0; } // Not used by root



    public static class Builder{

        UtilityTree root;
        Deque<Group> path = new ArrayDeque<>();


        public Builder(){
            root = new UtilityTree();
            path.push(root);
        }

        public Builder addNode(Node<Blackboard> node){
            path.peek().addChild(node);
            return this;
        }

        public Builder addGroup(Group group){
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
