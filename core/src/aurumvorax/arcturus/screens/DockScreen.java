package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.Menu;
import aurumvorax.arcturus.menus.shipyard.Shipyard;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DockScreen extends ScreenAdapter{

    private Menu menu;
    private static TextureRegion background;

    public DockScreen(Core core){
        menu = new Menu(core);
    }


    public static void setBackground(TextureRegion bg){ background = bg;}

    @Override
    public void show(){
        menu.enter(Shipyard.getInstance(), background);
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
