package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Physics2D extends Component{

    public Vector2 p = new Vector2();
    public Vector2 v = new Vector2();
    public float theta;
    public float omega;

    public void load(Physics2D source){
        p.set(source.p);
        v.set(source.v);
        theta = source.theta;
        omega = source.omega;
    }
}
