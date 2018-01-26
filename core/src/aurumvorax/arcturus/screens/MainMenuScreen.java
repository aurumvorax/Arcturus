package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.Menu;
import aurumvorax.arcturus.menus.main_menu.MainMenu;
import com.badlogic.gdx.ScreenAdapter;

public class MainMenuScreen extends ScreenAdapter{

    private Menu menu;

    public MainMenuScreen(Core core){
        menu = new Menu(core);
    }

    @Override
    public void show(){
        menu.enter(MainMenu.getInstance());
    }

    @Override
    public void render(float delta){
        menu.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        menu.resize(width, height);
    }

    @Override
    public void dispose() {
        menu.dispose();
    }
}
