package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

public abstract class Action{

    int[] axes;
    float weight;


    public Action(){
    }

    abstract void execute();
}
