package aurumvorax.arcturus.artemis.components;

import aurumvorax.arcturus.artemis.systems.render.Renderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Beam extends Weapon{

    public String imgName;
    public float offsetY;
    public Renderer.Layer layer = Renderer.Layer.EFFECT;

    public float length;
    public float dps;
    public boolean active = false;
    public Vector2 origin = new Vector2();
    public Vector2 unitBeam = new Vector2(1, 0);
    public Array<Vector2> barrels;

    public Vector2 contactPoint = new Vector2();
    public int contact;
}
