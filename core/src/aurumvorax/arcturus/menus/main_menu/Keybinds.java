package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Keybinds extends MenuState{

    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private Table menuTable = new Table();

    public Keybinds(){
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeBack();
            }
        });
    }

    @Override
    protected Actor build(Stage menuStage){
        menuTable.reset();
        menuTable.setFillParent(true);
        menuTable.add(backButton);
        return menuTable;
    }
}
