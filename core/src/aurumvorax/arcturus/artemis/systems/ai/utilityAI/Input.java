package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

public abstract class Input{

    String name;
    int ID;


    public Input(String name){
        this.name = name;
    }

    abstract float getData();
}
