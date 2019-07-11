package aurumvorax.arcturus.artemis.components;


import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Weapons extends Component{

    @EntityId public IntBag all = new IntBag();
    @EntityId public IntBag manual = new IntBag();
    @EntityId public IntBag auto = new IntBag();
    @EntityId public IntBag pd = new IntBag();
    @EntityId public Vector2 target = new Vector2();
}