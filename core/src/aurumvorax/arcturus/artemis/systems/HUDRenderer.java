package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import com.artemis.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HUDRenderer extends BaseEntitySystem implements RenderMarker{

    private Stage stage;
    private Label fps;
    private Image reticle;
    private static int targetID;
    private  Vector2 target;

    private static ComponentMapper<Physics2D> mPhysics;

    public HUDRenderer(){
        super(Aspect.all(PlayerShip.class));
        stage = new Stage(new ScreenViewport(), Services.batch);
        fps = new Label(String.format("%3d FPS", 0), Services.MENUSKIN);
        reticle = new Image(Services.getTexture("selector1"));
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(fps);
        stage.addActor(table);
        //stage.addActor(reticle);
    }

    public InputProcessor getInputProcessor(){ return stage; }
    public static void setTarget(int t){ targetID = t; }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    protected void processSystem(){
        // calculate stuff (every frame, not tick)

    }

    void render(float alpha){
        if(mPhysics.has(targetID)){
            target = mPhysics.get(targetID).p;
            reticle.setX(target.x);
            reticle.setY(target.y);
        }
        fps.setText(String.format("%3d FPS", Gdx.graphics.getFramesPerSecond()));
        stage.draw();
    }
}
