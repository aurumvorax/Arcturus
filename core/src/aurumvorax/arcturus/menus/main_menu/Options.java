package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.menus.MenuPage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Options extends MenuPage{

    private TextButton backButton = new TextButton("Back", Services.MENUSKIN);
    private Table menuTable = new Table();

    public Options(){
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
