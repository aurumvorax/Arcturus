package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

public abstract class Action{

    int[] axes;
    float weight;


    protected abstract boolean execute(float axisData[]);

    float calculateScore(float[] axisData){
        float tally = 1;

        for(int axis : axes)
            tally *= axisData[axis];

        return tally * weight;
    }
}
