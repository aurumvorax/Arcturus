package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.background_temp.Background;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;


public class RenderBatcher{

    private Array<Bag<Job>> jobs;
    private WorldCam camera;
    private HUDRenderer hud;
    private Background bg = new Background();


    public RenderBatcher(WorldCam camera, HUDRenderer hud){
        jobs = new Array<>();
        for(Renderer.Layer layer : Renderer.Layer.values()){
            jobs.add(new Bag<>());
        }
        this.camera = camera;
        this.hud = hud;
    }

    void register(int entityID, Renderer delegate, Renderer.Layer layer){
        jobs.get(layer.ordinal()).add(new Job(entityID, delegate));
    }

    void unregister(int entityID, Renderer delegate, Renderer.Layer layer){
        final Object[] list = jobs.get(layer.ordinal()).getData();
        for(int i = 0; i< list.length; i++){
            if(((Job)list[i]).entityID == entityID && ((Job)list[i]).delegate == delegate){
                jobs.get(layer.ordinal()).remove(i);
                break;
            }
        }
    }

    public void draw(float alpha){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Services.batch.setColor(1,1,1,1);
        Services.batch.setProjectionMatrix(camera.getMatrix(alpha));
        camera.setCullingFrame();
        Services.batch.begin();
        bg.draw(Services.batch, WorldCam.lerpX(alpha), WorldCam.lerpY(alpha));

        for(int layer = 0; layer < Renderer.Layer.values().length; layer++){
            for(Job job : jobs.get(layer)){
                job.delegate.draw(job.entityID, alpha);
            }
        }
        Services.batch.end();
        camera.endCullingFrame();
        hud.render(alpha);
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
