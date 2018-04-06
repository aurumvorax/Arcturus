package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import com.artemis.*;
import com.badlogic.gdx.*;
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
    private static int targetID = -1;
    private Vector2 target = new Vector2();
    private Vector2 targetLerp = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;

    public HUDRenderer(){
        super(Aspect.all(Player.class));
        stage = new Stage(new ScreenViewport(), Services.batch);
        fps = new Label(String.format("%3d FPS", 0), Services.MENUSKIN);
        reticle = new Image(Services.getTexture("selector1"));
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(fps);
        stage.addActor(table);
        stage.addActor(reticle);
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
        if((targetID != -1) &&(mPhysics.has(targetID))){
            reticle.setVisible(true);
            Physics2D physics = mPhysics.get(targetID);
            targetLerp.set(physics.p).mulAdd(physics.v, alpha);
            target.set(WorldCam.projectToStage(targetLerp, alpha));
            reticle.setX(target.x - (reticle.getImageWidth() * 0.5f));
            reticle.setY(target.y - (reticle.getImageHeight() * 0.5f));
        }else
            reticle.setVisible(false);

        fps.setText(String.format("%3d FPS", Gdx.graphics.getFramesPerSecond()));
        stage.draw();
    }
}
