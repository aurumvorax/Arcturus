package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.savegame.SaveObserver;
import aurumvorax.arcturus.savegame.SaveSubject;
import com.artemis.BaseSystem;

public class WorldSerializer extends BaseSystem implements SaveObserver{

    public WorldSerializer(){

    }


    @Override
    public void onNotify(SaveSubject saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case LOADING:
                System.out.println("Loading");
                break;
            case SAVING:
                System.out.println("Saving");
                break;
        }
    }

    @Override
    protected void processSystem(){}    // This system does not tick.
}
