package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.aiUtree.Node;
import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class AIData extends Component{

    @EntityId public int selfID;
    @EntityId public int targetID;

    public Node currentAction;


    public void setCurrent(Node n){
        currentAction = n;
    }
}
