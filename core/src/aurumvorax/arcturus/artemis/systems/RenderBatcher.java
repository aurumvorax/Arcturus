package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;


public class RenderBatcher{

    private Array<Bag<Job>> jobs;
    private WorldCam camera;


    public RenderBatcher(WorldCam camera){
        jobs = new Array<>();
        for(Renderer.Layer layer : Renderer.Layer.values()){
            jobs.add(new Bag<>());
        }
        this.camera = camera;
    }

    public void register(int entityID, Renderer delegate, int layer){
        jobs.get(layer).add(new Job(entityID, delegate));
    }

    public void unregister(int entityID, Renderer delegate, int layer){
        final Object[] list = jobs.get(layer).getData();
        for(int i = 0; i< list.length; i++){
            if(((Job)list[i]).entityID == entityID && ((Job)list[i]).delegate == delegate){
                jobs.get(layer).remove(i);
                break;
            }
        }
    }

    public void draw(float alpha){

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Services.batch.setProjectionMatrix(camera.getMatrix(alpha));
        Services.batch.begin();

        for(int layer = 0; layer < Renderer.Layer.values().length; layer++){
            for(Job job : jobs.get(layer)){
                job.delegate.draw(job.entityID, alpha);
            }
        }
        Services.batch.end();
    }


    private static class Job{

        int entityID;
        Renderer delegate;

        Job(int entityID, Renderer delegate){
            this.entityID = entityID;
            this.delegate = delegate;
        }
    }
}
