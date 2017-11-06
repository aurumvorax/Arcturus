package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.artemis.systems.Renderer;
import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Beam extends Component{

    public String name;
    public float offsetY;
    public Renderer.Layer layer = Renderer.Layer.EFFECT;

    public float length;
    public float maxRange;
    public boolean active = false;
    public Vector2 origin = new Vector2();
    public Vector2 unitBeam = new Vector2(1, 0);
    public Array<Vector2> barrels;

}
