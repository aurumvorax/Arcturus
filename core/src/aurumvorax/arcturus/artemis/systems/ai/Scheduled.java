package aurumvorax.arcturus.artemis.systems.ai;

import com.artemis.World;

public interface Scheduled{

    void init(World world);

    void runTask(int entityId);
}
