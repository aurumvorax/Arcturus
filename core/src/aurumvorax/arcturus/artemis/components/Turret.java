package aurumvorax.arcturus.artemis.components;

import com.artemis.Component;

public class Turret extends Component{

    private static final float FIXED_ARC = 5;

    public float arcMin;
    public float arcMax;
    public float arcMid;
    public float omegaMax;
    public boolean fire;
    public float target;

    public void setArcs(float angle, float arc){
        arcMid = angle;
        if(arc == 0){       // Fixed hardpoint
            arcMin = angle - FIXED_ARC;
            arcMax = angle + FIXED_ARC;
        }else{              // Turret mount
            arcMin = angle - (arc * 0.5f);
            arcMax = angle + (arc * 0.5f);
        }
    }
}