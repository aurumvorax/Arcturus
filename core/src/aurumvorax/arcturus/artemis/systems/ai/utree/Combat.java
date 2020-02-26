package aurumvorax.arcturus.artemis.systems.ai.utree;

import aurumvorax.arcturus.aiUtree.Group;
import aurumvorax.arcturus.artemis.components.AIData;

public class Combat extends Group{

    @Override
    protected float evaluate(AIData bb){
        if(false)  // hostiles close?
            return 4;

        return 0;
    }
}
