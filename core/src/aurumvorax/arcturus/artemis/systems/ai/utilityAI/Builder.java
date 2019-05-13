package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class Builder{

    private Array<Input> inputs = new Array<>();
    private Array<Transform> transforms = new Array<>();
    private IntMap<String> inputNames = new IntMap<>();

    private ActionBuilder root;
    private ActionBuilder currentGroup;


    public Builder(){
        root = new ActionBuilder();
        root.children = new Array<>();
        currentGroup = root;
    }

    public Builder addInput(Input i, Transform t){
        i.ID = inputs.size;
        inputs.add(i);
        transforms.add(t);
        inputNames.put(i.ID, i.getName());

        return this;
    }

    public Builder atartGroup(float weight, String ... inputs){
        ActionBuilder group = new ActionBuilder();

        group.weight = weight;
        group.inputs = inputs;
        group.parent = currentGroup;
        group.children = new Array<>();

        currentGroup.children.add(group);
        currentGroup = group;

        return this;
    }

    public Builder endGroup(){
        if(currentGroup == root)
            Gdx.app.debug("AI Builder", "No need to close root group - just use build()");
        else
            currentGroup = currentGroup.parent;

        return this;
    }

    public Builder addAction(Action a, float weight, String ... inputs){
        ActionBuilder builder = new ActionBuilder();

        builder.a = a;
        builder.weight = weight;
        builder.inputs = inputs;

        currentGroup.children.add(builder);

        return this;
    }

    public Core build(){
        return new Core(inputs.items, transforms.items, buildGroup(root));
    }

    private ActionGroup buildGroup(ActionBuilder builder){
        ActionGroup group = (ActionGroup)buildAction(builder);

        Action[] actions = new Action[builder.children.size];

        for(int i = 0; i < builder.children.size; i++)
            actions[i] = buildAction(builder.children.get(i));

        group.actions = actions;

        return group;
    }

    private Action buildAction(ActionBuilder builder){
        Action a = builder.a;

        a.axes = new int[builder.inputs.length];

        for(int j = 0; j < builder.inputs.length; j++){
            int id = inputNames.findKey(builder.inputs[j], false, -1);

            if(id == -1)
                throw new IllegalArgumentException("AI Builder - Cannot find input - " + builder.inputs[j]);

            a.axes[j] = id;
            a.weight = builder.weight;
        }

        return a;
    }


    private static class ActionBuilder{
        Action a;
        float weight;
        String[] inputs;
        ActionBuilder parent;
        Array<ActionBuilder> children;
    }
}
