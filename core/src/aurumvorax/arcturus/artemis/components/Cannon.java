package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Cannon extends Component{

    public String launches;
    public float timer;
    public float burstTime;
    public float reloadTime;
    public int barrel = 0;
    public Array<Vector2> barrels;
    public float maxRange;
    public float speed;
}
