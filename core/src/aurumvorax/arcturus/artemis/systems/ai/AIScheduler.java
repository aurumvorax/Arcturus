package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.aiUtree.ScheduledUTree;
import aurumvorax.arcturus.aiUtree.UtilityTree;
import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Player;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;


public class AIScheduler extends BaseEntitySystem{

    private static final int SPREAD = 10;

    private static ComponentMapper<AIData> mData;

    private IntBag ids = new IntBag();
    private IntBag removed = new IntBag();
    private Array<Scheduled> tasks = new Array<>();
    private UtilityTree utree;
    private int frame;
    private int groupSize;


    @SuppressWarnings("unchecked")
    public AIScheduler(){
        super(Aspect.all(AIData.class).exclude(Player.class));
    }

    @Override protected void inserted(int entityID){ ids.add(entityID); }
    @Override protected void removed(int entityID){ removed.add(entityID); }

    @Override
    public void initialize(){

        // tasks.add(new Detection());
        // etc
        tasks.add(new Detection());
        tasks.add(new ScheduledUTree());

        for(Scheduled t : tasks)
            t.init(world);
    }

    @Override
    protected void processSystem(){
        if(frame == 0){
            for(int i = 0; i < removed.size(); i++){
                ids.removeValue(removed.get(i));
            }
            removed.clear();

            groupSize = Math.max(1, ids.size() / (SPREAD - 1));
        }

        for(int j = frame * groupSize; (j < ((frame + 1) * groupSize) && (j < ids.size())); j++){
            for(Scheduled t : tasks){
                if(mData.has(ids.get(j)))
                    t.runTask(ids.get(j));
            }
        }

        if(frame++ >= SPREAD)
            frame = 0;

        for(int k = 0; k < ids.size(); k++){
            if(mData.has(ids.get(k))){
                AIData d = mData.get(ids.get(k));

                if(d.currentAction != null)
                    d.currentAction.execute(d);
            }
        }
    }
}
