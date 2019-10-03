package aurumvorax.arcturus.artemis.components;


import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.utils.IntBag;

public class Weapons extends Component{

    @EntityId public IntBag all = new IntBag();
    @EntityId public IntBag main = new IntBag();
    @EntityId public IntBag auto = new IntBag();
    @EntityId public IntBag pd = new IntBag();
}
