package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.menus.death.GameOver;
import aurumvorax.arcturus.menus.main_menu.Keybinds;
import aurumvorax.arcturus.menus.main_menu.Options;
import aurumvorax.arcturus.menus.main_menu.SaveLoad;
import aurumvorax.arcturus.menus.map.GalaxyMap;
import aurumvorax.arcturus.menus.shipyard.Shipyard;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;

public class MenuScreen extends ScreenAdapter{

    private static TextureRegion background;
    private static TextureRegion liveBackground;

    private Core core;
    private Stage stage = new Stage(new ScreenViewport(), Services.batch);
    private Deque<MenuType> stateStack = new ArrayDeque<>();
    private MenuType current;

    private EnumMap<MenuType, MenuPage> menus = new EnumMap<>(MenuType.class);
    public enum MenuType{
        Main, MainLive, Save, Load, Options, Keybinds,
        Shipyard, Map,  // Dock, Missions, Commerce, InfoBroker, etc
        Dead
    }

    public MenuScreen(Core core){
        this.core = core;

        MenuPage mainMenu = new MainMenu();
        menus.put(MenuType.Main, mainMenu);
        menus.put(MenuType.MainLive, mainMenu);
        MenuPage saveLoad = new SaveLoad();
        menus.put(MenuType.Save, saveLoad);
        menus.put(MenuType.Load, saveLoad);
        menus.put(MenuType.Options, new Options());
        menus.put(MenuType.Keybinds, new Keybinds());

        menus.put(MenuType.Shipyard, new Shipyard());
        menus.put(MenuType.Map, new GalaxyMap());
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

    // To be called by TransitionManager via USTCore, prior to transition.
    public void enterMenu(MenuType menu){
        current = menu;
        MenuPage state = menus.get(menu);
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



    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Services.batch.setColor(1,1,1,1);

        if(background != null){
            Services.batch.begin();
            Services.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Services.batch.end();
        }

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
