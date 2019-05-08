package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

public class Core{

    private Input[] inputs;
    private Transform[] transforms;
    private Action[][] groups;

    private float[] inputData;
    private float[] axisData;


    Core(Input[] inputs, Transform[] transforms, Action[][] groups){
        this.inputs = inputs;
        this.transforms = transforms;
        this.groups = groups;

        inputData = new float[inputs.length];
        axisData = new float[inputs.length];
    }

    public void tick(){
        for(int i = 0; i < inputs.length; i++)
            inputData[i] = inputs[i].getData();

        for(int j = 0; j < transforms.length; j++)
            axisData[j] = transforms[j].calcTransform(inputData[j]);

        for(int k = 0; k < groups.length; k++)
            calculateGroup(groups[k]);
    }

    private void calculateGroup(Action[] actions){
        float[] scores = new float[actions.length];
        float bestScore = 0;
        Action bestAction = null;

        for(int l = 0; l < actions.length; l++){
            scores[l] = calculateAction(actions[l]);

            if(scores[l] > bestScore){
                bestScore = scores[l];
                bestAction = actions[l];
            }
        }

        if(bestAction != null)
            bestAction.execute();
    }

    private float calculateAction(Action a){
        float tally = 1;

        for(int axis : a.axes)
            tally *= axisData[axis];

        return tally * a.weight;
    }
}
