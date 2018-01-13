package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;

public class Orbit extends Component{
    @EntityId public int parent;
    public Vector2 center = new Vector2();
    public float major;
    public float minor;
    public float tilt;
    public float time;
    public double sweep;

}
