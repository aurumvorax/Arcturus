package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.artemis.components.Health;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.codex.Codex;
import aurumvorax.arcturus.services.Services;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HUDRenderer extends BaseEntitySystem implements RenderMarker{

    private Stage stage;
    private MenuFramework frame;
    private Label fps, health;
    private Image reticle;
    private static int targetID = -1;
    private Vector2 target = new Vector2();
    private Vector2 targetLerp = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Health> mHealth;


    public HUDRenderer(){
        super(Aspect.all(Player.class));
        stage = new Stage(new ScreenViewport(), Services.batch);
        frame = new MenuFramework();
        fps = new Label(String.format("%3d FPS", 0), Services.getSkin());
        health = new Label(String.format("Health : %3.2f", 0f), Services.getSkin());
        reticle = new Image(Services.getTexture("selector1"));
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(fps);
        stage.addActor(table);
        stage.addActor(reticle);
        stage.addActor(health);
        stage.addActor(frame);
    }

    public InputProcessor getInputProcessor(){ return stage; }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    public void showMenu(MenuFramework.Page page){
        frame.openToPage(page);
    }

    public void showCodex(Codex.Page page){
        frame.setCodex(page);
        frame.openToPage(MenuFramework.Page.Codex);
    }

    @Override
    protected void processSystem(){
        targetID = PlayerShip.getTargetID();

        stage.act(world.getDelta());
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
        if(mHealth.has(PlayerShip.getID()))
            health.setText(String.format("Health : %3.2f", mHealth.get(PlayerShip.getID()).hull));

        stage.draw();
    }
}
