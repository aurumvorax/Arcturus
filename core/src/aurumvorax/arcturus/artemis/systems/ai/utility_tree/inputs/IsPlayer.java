package aurumvorax.arcturus.artemis.systems.ai.utility_tree.inputs;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.Input;
import com.artemis.ComponentMapper;

public class IsPlayer extends Input{

    private static ComponentMapper<Player> mPlayer;


    @Override
    protected float getData(AIData bb){
        return (mPlayer.has(bb.selfID)) ? 1f : 0f;
    }
}
