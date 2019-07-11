package aurumvorax.arcturus.artemis.systems.ai.utility_tree.nodes;

import aurumvorax.arcturus.artemis.systems.ai.utility_tree.Blackboard;
import aurumvorax.arcturus.artemis.systems.ai.utility_tree.Node;
import com.badlogic.gdx.Gdx;

public class Attack extends Node{

    @Override
    protected void action(Blackboard bb){
        Gdx.app.log("", "Attack!");
    }
}
