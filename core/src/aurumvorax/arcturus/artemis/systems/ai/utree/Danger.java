package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Group;
import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Sensors;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public class Danger extends Group{

    private static final float closeRange = 800f;

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Physics2D> mPhysics;


    // detected hostiles near/within weapons range or damage taken from another ship
    @Override
    protected float evaluate(AIData bb){
        IntBag hostiles = mSensors.get(bb.selfID).hostileShips;
        Vector2 pos = mPhysics.get(bb.selfID).p;

        for(int i = 0; i < hostiles.size(); i++){
            float dst2 = pos.dst2(mPhysics.get(hostiles.get(i)).p);
            if(dst2 < (closeRange * closeRange))
                return 4;
        }

        return 0;
    }
}
