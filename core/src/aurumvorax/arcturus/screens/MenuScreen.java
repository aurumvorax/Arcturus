package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.menus.main_menu.MainMenu;
import aurumvorax.arcturus.menus.shipyard.Shipyard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.*;

public class MenuScreen extends ScreenAdapter{

    private static TextureRegion background = Services.getTexture("MainMenuBackground");

    private Core core;
    private Stage stage = new Stage(new ScreenViewport(), Services.batch);
    private Deque<MenuState> stateStack = new ArrayDeque<>();
    private MenuState currentState;

    private EnumMap<MenuType, MenuState> menus = new EnumMap<>(MenuType.class);
    public enum MenuType{
        Main,
        Dock,
        Dead
    }

    public MenuScreen(Core core){
        this.core = core;
        menus.put(MenuType.Main, new MainMenu());
        menus.put(MenuType.Dock, new Shipyard());
        //menus.put(MenuType.Dead, new GameOver());

        currentState = menus.get(MenuType.Main);
    }

    // To be called by TransitionManager prior to transition.
    public void setMenu(MenuType menu){
        currentState = menus.get(menu);
        stage.addActor(currentState.enter());
        // TODO set appropriate backgrounds.
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
    }

    public void changeMenu(MenuState newState){
        stage.getRoot().setColor(1,1,1,1);
        stage.clear();
        stateStack.push(currentState);
        currentState = newState;
        stage.addActor(newState.enter());
    }
    public void changeBack(){
        stage.getRoot().setColor(1,1,1,1);
        stage.clear();
        stateStack.pop();
        if(stateStack.peek() == null)
            enterGame(); // no previous state, so resume game
        else
            stage.addActor(stateStack.peek().enter());
    }

    public void enterGame(){
        Gdx.input.setInputProcessor(null);
        stage.getRoot().getColor().a = 1f;
        stage.clear();
        stateStack.clear();
        TransitionManager.resumeGame();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Services.batch.setColor(1,1,1,1);
        Services.batch.begin();
        Services.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Services.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
