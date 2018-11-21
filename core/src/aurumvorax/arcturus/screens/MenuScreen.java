package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.menus.death.GameOver;
import aurumvorax.arcturus.menus.main_menu.Keybinds;
import aurumvorax.arcturus.menus.main_menu.MainMenu;
import aurumvorax.arcturus.menus.main_menu.Options;
import aurumvorax.arcturus.menus.main_menu.SaveLoad;
import aurumvorax.arcturus.menus.shipyard.Shipyard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.*;

public class MenuScreen extends ScreenAdapter{

    private static TextureRegion background;
    private static TextureRegion liveBackground;

    private Core core;
    private Stage stage = new Stage(new ScreenViewport(), Services.batch);
    private Deque<MenuType> stateStack = new ArrayDeque<>();
    private MenuType current;

    private EnumMap<MenuType, MenuState> menus = new EnumMap<>(MenuType.class);
    public enum MenuType{
        Main, MainLive, Save, Load, Options, Keybinds,
        Shipyard,   // Dock, Missions, Commerce, Intel, etc
        Dead
    }

    public MenuScreen(Core core){
        this.core = core;

        MenuState mainMenu = new MainMenu();
        menus.put(MenuType.Main, mainMenu);
        menus.put(MenuType.MainLive, mainMenu);
        MenuState saveLoad = new SaveLoad();
        menus.put(MenuType.Save, saveLoad);
        menus.put(MenuType.Load, saveLoad);
        menus.put(MenuType.Options, new Options());
        menus.put(MenuType.Keybinds, new Keybinds());

        menus.put(MenuType.Shipyard, new Shipyard());

        menus.put(MenuType.Dead, new GameOver());

        current = MenuType.Main;
    }

    public void setCurrent(MenuType menu){ current = menu; }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
        liveBackground = ScreenUtils.getFrameBufferTexture(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        enterMenu(current);
    }

    // To be called by TransitionManager via Core, prior to transition.
    public void enterMenu(MenuType menu){
        current = menu;
        MenuState state = menus.get(menu);
        switch(menu){
            case Main:  // This is the Main menu used during startup, or when accessed from other menus
                background = Services.getTexture("MainMenuBackground");
                break;
            case MainLive:  // This is the Main menu accessed from inside the game world.
                background = liveBackground;
                break;
            case Save:  // Background is carried over from Main or MainLive
                ((SaveLoad)state).setMode(SaveLoad.Mode.SAVE);
                break;
            case Load:  // Background is carried over from Main or MainLive
                ((SaveLoad)state).setMode(SaveLoad.Mode.LOAD);
                break;
            case Shipyard:
                background = Services.getTexture("ShipyardBackground");
                break;
            case Dead:
                background = liveBackground;
                break;
        }

        stage.addActor(state.enter(core, this, stage));
    }

    public void changeMenu(MenuType newMenu){
        stage.getRoot().setColor(1,1,1,1);
        stage.clear();
        stateStack.push(current);
        enterMenu(newMenu);
    }

    public void changeBack(){
        stage.getRoot().setColor(1,1,1,1);
        stage.clear();
        if(stateStack.peek() == null)
            enterGame(Core.GameMode.Active); // no previous state, so resume game
        else
            enterMenu(stateStack.pop());
    }

    public void enterGame(Core.GameMode state){
        Gdx.input.setInputProcessor(null);
        stage.getRoot().getColor().a = 1f;
        stage.clear();
        stateStack.clear();
        core.setGameMode(state);
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
        menus.get(current).resize(width, height);
    }

    @Override
    public void hide(){
        liveBackground.getTexture().dispose();
    }

    @Override
    public void dispose(){
        stage.dispose();
        menus.clear();
    }
}
