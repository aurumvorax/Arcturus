package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Options extends MenuPage{

    private TextButton keysButton = new TextButton("Keybinds", Services.MENUSKIN);
    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private Table menuTable = new Table();

    public Options(){

        keysButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Keybinds);
            }
        });

        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeBack();
            }
        });
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        menuTable.setFillParent(true);
        menuTable.add(keysButton).row();
        menuTable.add(backButton);

        add(menuTable);
    }
}
