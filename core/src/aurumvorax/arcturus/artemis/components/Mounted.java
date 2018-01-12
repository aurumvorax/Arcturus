package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;

public class Mounted extends Component{

    @EntityId public int parent;
    public Vector2 location = new Vector2();
    public Vector2 position = new Vector2();
    public float theta;
    public float omega;
}
