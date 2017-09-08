package aurumvorax.arcturus.artemis.components.shipComponents;


import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

public class Weapons extends Component{

    public IntArray all = new IntArray();
    public IntArray active = new IntArray();
    public Vector2 target = new Vector2();
    public boolean fire = false;
}
