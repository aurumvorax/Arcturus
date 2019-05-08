package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;

public class Builder{

    private Array<Input> inputs = new Array<>();
    private Array<Transform> transforms = new Array<>();
    private IntMap<String> inputNames = new IntMap<>();

    private Array<Array<ActionBuilder>> groups = new Array<>();
    private Array<ActionBuilder> currentGroup = new Array<>();


    public Builder addInput(Input i, Transform t){
        i.ID = inputs.size;
        inputs.add(i);
        transforms.add(t);
        inputNames.put(i.ID, i.name);

        return this;
    }

    public Builder addGroup(){
        currentGroup = new Array<>();
        groups.add(currentGroup);

        return this;
    }

    public Builder addAction(Action a, float weight, String ... inputs){
        a.weight = weight;
        ActionBuilder ab = new ActionBuilder(a, inputs);
        currentGroup.add(ab);

        return this;
    }

    public Core build(){
        Array<Action[]> actionGroups = new Array<>();
        for(Array<ActionBuilder> group : groups){

            Array<Action> actions = new Array<>();
            for(ActionBuilder ab : group){

                IntArray ids = new IntArray();
                for(String name : ab.inputs){
                    int id = inputNames.findKey(name, false, -1);

                    if(id == -1)
                        Gdx.app.error("AI Builder", "Cannot find input - " + name);
                    else
                        ids.add(id);
                }

                if(ids.size == 0)
                    Gdx.app.error("AI Builder", "Cannot create action with no inputs - " + ab);
                else{
                    ab.a.axes = ids.items;
                    actions.add(ab.a);
                }
            }

            if(actions.size == 0)
                Gdx.app.error("AI Builder", " Cannot have an action group with no actions - " + group);
            else{
                actionGroups.add(actions.items);
            }
        }

        if(actionGroups.size == 0){
            Gdx.app.error("AI Builder", "No actions or action groups registered");
            return null;
        }else
            return new Core(inputs.items, transforms.items, actionGroups.items);
    }


    private static class ActionBuilder{
        Action a;
        String[] inputs;

        ActionBuilder(Action a, String[] i){
            this.a = a;
            this.inputs = i;
        }
    }
}
