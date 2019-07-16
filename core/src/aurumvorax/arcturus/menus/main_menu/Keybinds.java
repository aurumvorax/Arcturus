package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Keybinds extends MenuPage{

    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private Table menuTable = new Table();

    public Keybinds(MenuFramework frame){
        super(frame);

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
        menuTable.add(backButton);

        add(menuTable);

    }
}
