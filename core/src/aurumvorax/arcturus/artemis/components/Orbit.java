package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class Orbit extends Component{
    @EntityId public int parent;
    public float speed;

}
