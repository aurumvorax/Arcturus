package aurumvorax.arcturus.artemis.systems.ai;

import com.artemis.World;

public interface Scheduled{

    default void init(World world){
        world.inject(this);
    }

    void runTask(int entityId);
}
