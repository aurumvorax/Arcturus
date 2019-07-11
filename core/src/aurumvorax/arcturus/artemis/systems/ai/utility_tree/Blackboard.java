package aurumvorax.arcturus.artemis.systems.ai.utility_tree;

public class Blackboard{

    public int selfID;
    public int targetID;

    void reset(int selfID){
        this.selfID = selfID;
        targetID = -1;
    }

}
