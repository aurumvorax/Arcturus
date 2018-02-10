package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Menu{

    private Core core;
    private Stage stage = new Stage(new ScreenViewport(), Services.batch);

    // background
    private StackStateMachine<Menu, MenuState> stateMachine = new StackStateMachine<>(this);

    public Menu(Core core){
        this.core = core;
    }

    public Stage getStage(){ return stage; }

    public void enter(MenuState initial){
        stateMachine.changeState(initial);
        Gdx.input.setInputProcessor(stage);
    }

    public void changeMenu(MenuState menu){
        stage.clear();
        stateMachine.changeState(menu);
    }

    public void changeBack(){
        stage.clear();
        if(!stateMachine.revertToPreviousState())
            core.switchScreen(Core.ScreenType.Game);
    }

    public void exitTo(Core.ScreenType screen){
        Gdx.input.setInputProcessor(null);
        stage.clear();
        core.switchScreen(screen);
    }

    public void update(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw background

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    public void dispose(){
        stage.dispose();
    }
}
