package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.artemis.utils.IntBag;

public class Sensors extends Component{

    public float sensorPower = 2000;
    public float visibility = 1.0f;
    public boolean beacon = false;
    public boolean scanForMissiles;

    public IntBag friendlyShips = new IntBag();
    public IntBag neutralShips = new IntBag();
    public IntBag hostileShips = new IntBag();
    public IntBag nonfriendlyMissiles = new IntBag();


    public void reset(){
        friendlyShips.clear();
        neutralShips.clear();
        hostileShips.clear();
        nonfriendlyMissiles.clear();
    }
}
