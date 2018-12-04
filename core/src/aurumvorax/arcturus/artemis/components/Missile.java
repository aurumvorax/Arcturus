package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class Missile extends Component{

    @EntityId public int target = -1;
    public float engineDuration;
}
