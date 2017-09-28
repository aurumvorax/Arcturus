package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.savegame.SaveObserver;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.utils.IntBag;

public class WorldSerializer extends BaseSystem implements SaveObserver{

    private IntBag allEntities;

    public WorldSerializer(){

    }


    @Override
    public void onNotify(SaveManager saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case LOADING:
                System.out.println("Loading");
                allEntities = saveManager.getProperty("Entities", IntBag.class);
                System.out.println(allEntities);
                break;

            case SAVING:
                System.out.println("Saving");

                // get master list of entities
                allEntities = world.getAspectSubscriptionManager()
                    .get(Aspect.all())
                    .getEntities();
                saveManager.setProperty("Entities", allEntities);
                System.out.println(allEntities);
                break;
        }
    }

    @Override
    protected void processSystem(){}    // This system does not tick.
}
