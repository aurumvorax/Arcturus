package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.savegame.ArrayKryoSerializer;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.savegame.SaveObserver;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.io.KryoArtemisSerializer;
import com.artemis.io.SaveFileFormat;
import com.artemis.utils.IntBag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class WorldSerializer extends BaseSystem implements SaveObserver{

    private KryoArtemisSerializer backend;

    public WorldSerializer(){}

    public void init(){
        backend = new KryoArtemisSerializer(world);
        ArrayKryoSerializer.registerArraySerializer(backend.getKryo());
    }

    @Override
    public void onNotify(SaveManager saveManager, SaveEvent saveEvent){
        switch(saveEvent){
            case SAVING:
                IntBag entityIDs = world.getAspectSubscriptionManager()
                    .get(Aspect.all())
                    .getEntities();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                backend.save(os, new SaveFileFormat(entityIDs));
                saveManager.saveElement("Artemis", os.toByteArray());
                break;

            case LOADING:
                resetWorld();
                ByteArrayInputStream is = new ByteArrayInputStream(saveManager.loadElement("Artemis", byte[].class));
                backend.load(is, SaveFileFormat.class);
                break;
        }
    }

    public void resetWorld(){
        IntBag entities = world.getAspectSubscriptionManager()
                .get(Aspect.all())
                .getEntities();

        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            world.delete(ids[i]);
        }
        world.process();
        world.getEntityManager().reset();
    }

    @Override
    protected void processSystem(){}    // This system does not tick.
}
