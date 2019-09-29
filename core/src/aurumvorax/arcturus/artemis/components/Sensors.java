package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.utils.IntBag;

public class Sensors extends Component{

    public float range = 1000;
    public boolean beacon = true;
    public boolean scanForMissiles;

    public IntBag ships = new IntBag();
    public IntBag missiles = new IntBag();
}
