package aurumvorax.arcturus.artemis.components.shipComponents;


import com.artemis.Component;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;

public class Weapons extends Component{

    public IntBag all = new IntBag();
    public IntBag manual = new IntBag();
    public IntBag auto = new IntBag();
    public IntBag pd = new IntBag();
    public Vector2 target = new Vector2();
}
