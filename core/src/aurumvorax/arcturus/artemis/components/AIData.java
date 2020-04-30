package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.aiUtree.Node;
import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class AIData extends Component{

    @EntityId public int selfID;
    @EntityId public int targetID;

    public boolean dirty = true;

    public float threatRange2;
    public float weaponRange;
    public float threatOffence;
    public float threatDefence;

    public Node currentAction;
    public void setCurrent(Node n){ currentAction = n; }
}
