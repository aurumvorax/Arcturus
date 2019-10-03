package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.utils.IntBag;

public class Sensors extends Component{

    public float range = 1000;
    public boolean beacon = true;
    public boolean scanForMissiles;

    public IntBag ships = new IntBag();
    public IntBag missiles = new IntBag();

    public IntBag friendlyShips = new IntBag();
    public IntBag neutralShips = new IntBag();
    public IntBag hostileShips = new IntBag();
    public IntBag hostileMissiles = new IntBag();


    public void reset(){
        friendlyShips.clear();
        neutralShips.clear();
        hostileShips.clear();
        hostileMissiles.clear();
    }
}
