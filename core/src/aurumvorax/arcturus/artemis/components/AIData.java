package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class AIData extends Component{

    @EntityId public int selfID;
    @EntityId public int targetID;
}
