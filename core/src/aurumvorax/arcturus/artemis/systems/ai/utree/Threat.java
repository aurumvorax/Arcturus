package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Group;
import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Sensors;
import com.artemis.ComponentMapper;

public class Threat extends Group{

    private static ComponentMapper<Sensors> mSensors;


    @Override
    protected float evaluate(AIData bb){
        if(mSensors.get(bb.selfID).hostileShips.size() > 0) // hostiles detected?
            return 2;
        return 0;
    }
}
