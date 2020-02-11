package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Turret extends Component{

    private static final float FIXED_ARC = 10;

    public float arcMin;
    public float arcMax;
    public float arcMid;
    public float omegaMax;
    public float sweepMin;
    public float sweepMax;

    public boolean fire = true;
    public int target = -1;

    public transient Vector2 targetPosition = new Vector2();

    public void setArcs(float angle, float arc){
        arcMid = angle;
        if(arc == 0){       // Fixed hardpoint
            arcMin = angle - (FIXED_ARC * 0.5f);
            arcMax = angle + (FIXED_ARC * 0.5f);
        }else{              // Turret mount
            arcMin = angle - (arc * 0.5f);
            arcMax = angle + (arc * 0.5f);
        }
    }
}
