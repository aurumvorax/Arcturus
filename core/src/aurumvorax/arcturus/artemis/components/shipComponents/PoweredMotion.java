package aurumvorax.arcturus.artemis.components.shipComponents;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PoweredMotion extends Component{

    public Vector2 accel = new Vector2();
    public float alpha = 0;
    public float thrust = 300;
    public float rotation = 100;
    public float maxO = 100;
    public float maxV = 300;
}
