package aurumvorax.arcturus.artemis.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Cannon extends Weapon{

    public String launches;
    public float timer;
    public float burstTime;
    public float reloadTime;
    public int barrel = 0;
    public Array<Vector2> barrels;
    public float duration;
    public float speed;
}
