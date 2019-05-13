package aurumvorax.arcturus.artemis.systems.ai.utilityAI;


public class Core{

    private Input[] inputs;
    private Transform[] transforms;

    private float[] inputData;
    private float[] axisData;
    private ActionGroup root;


    Core(Input[] inputs, Transform[] transforms, ActionGroup root){
        this.inputs = inputs;
        this.transforms = transforms;
        this.root = root;

        inputData = new float[inputs.length];
        axisData = new float[inputs.length];
    }

    public void tick(){
        for(int i = 0; i < inputs.length; i++)
            inputData[i] = inputs[i].getData();

        for(int j = 0; j < transforms.length; j++)
            axisData[j] = transforms[j].calcTransform(inputData[j]);

       root.execute(axisData);
    }
}
