package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class HUDRenderer extends BaseSystem implements RenderMarker{

    private OrthographicCamera camera;

    public HUDRenderer(){
        camera = new OrthographicCamera();
    }

    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    protected void processSystem(){
        Services.batch.setProjectionMatrix(camera.combined);
        Services.batch.begin();



        Services.batch.end();
    }


}
