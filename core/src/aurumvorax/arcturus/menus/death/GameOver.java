package aurumvorax.arcturus.menus.death;

import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOver extends MenuPage{
    private TextButton deadButton = new TextButton("You are dead.\nWalk it off.", Services.MENUSKIN);
    private Table menuTable = new Table();

    public GameOver(){
        deadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuScreen.MenuType.Main);
            }
        });
    }

    @Override
    protected Actor build(Stage menuStage){
        menuTable.reset();
        menuTable.setFillParent(true);
        menuTable.add(deadButton);
        return menuTable;
    }
}
