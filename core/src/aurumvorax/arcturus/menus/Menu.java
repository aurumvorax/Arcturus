package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayDeque;
import java.util.Deque;

public class Menu{

    private Core core;
    private Stage stage = new Stage(new ScreenViewport(), Services.batch);

    // background
    private Deque<MenuState> stateStack = new ArrayDeque<>();

    public Menu(Core core){
        this.core = core;
    }

    public Stage getStage(){ return stage; }

    public void enter(MenuState initial){
        Gdx.input.setInputProcessor(stage);
        stateStack.push(initial);
        stage.addActor(initial.enter(this, stage));
    }

    public void changeMenu(MenuState menu){
        stage.clear();
        stateStack.push(menu);
        stage.addActor(menu.enter(this, stage));
    }

    public void changeBack(){
        stage.clear();
        stateStack.pop();
        if(stateStack.peek() == null)
            exitTo(Core.ScreenType.Game);
        else
            stage.addActor(stateStack.peek().enter(this, stage));
    }

    public void exitTo(Core.ScreenType screen){
        Gdx.input.setInputProcessor(null);
        stage.getRoot().getColor().a = 1f;
        stage.clear();
        stateStack.clear();
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
