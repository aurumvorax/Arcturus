package aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.Node;
import com.badlogic.gdx.Gdx;

public class Attack extends Node{

    @Override
    protected void action(AIData bb){
        Gdx.app.log("", "Attack!");
    }
}
