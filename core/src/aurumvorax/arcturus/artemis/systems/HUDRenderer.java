package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import com.artemis.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HUDRenderer extends BaseEntitySystem implements RenderMarker{

    private Stage stage;
    private Label fps;

    public HUDRenderer(){
        super(Aspect.all(PlayerShip.class));
        stage = new Stage(new ScreenViewport());
        fps = new Label(String.format("%3d FPS", 0), Services.MENUSKIN);
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(fps);
        stage.addActor(table);
    }

    public InputProcessor getInputProcessor(){ return stage; }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    protected void processSystem(){
        // calculate stuff (every frame, not tick)

    }

    void render(float alpha){
        fps.setText(String.format("%3d FPS", Gdx.graphics.getFramesPerSecond()));
        stage.draw();
    }
}
