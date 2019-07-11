package aurumvorax.arcturus.artemis.systems.ai.utility_tree;

import aurumvorax.arcturus.artemis.components.AIData;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;

public class UtilityTree extends Node{

    private Input[] inputs;


    private UtilityTree(){}  // Enforce use of Builder

    @Override
    protected void action(AIData bb){}

    float getInputData(int index, AIData bb){
        return inputs[index].getData(bb);
    }


    public static class Builder{

        World world;
        UtilityTree tree;
        Node current;
        Array<Input> inputs = new Array<>(true, 10);
        Array<String> inputNames = new Array<>(true, 10);


        public Builder(World world){
            this.world = world;
            tree = new UtilityTree();
            current = tree;
        }


        public Builder addInput(String name, Input input){
            world.inject(input);
            inputs.add(input);
            inputNames.add(name);

            return this;
        }

        public Builder addNode(Node node){
            node.root = tree;
            node.parent = current;

            current.children.add(node);
            current = node;

            return this;
        }

        public Builder linkInput(String input, float weight){
            if(current == tree)
                throw new IllegalArgumentException("Cannot add input conditions to tree root.  Add a Node first.");

            int index = inputNames.indexOf(input, false);
            if(index == -1)
                throw new IllegalArgumentException(input + " is not a valid input.  Use AddInput first.");

            current.inputIDs.add(index);
            current.weights.add(weight);

            return this;
        }

        public Builder end(){
            if(current.parent != null)
                current = current.parent;

            return this;
        }

        public UtilityTree build(){
            tree.inputs = inputs.toArray(Input.class);

        return tree;
        }
    }
}
