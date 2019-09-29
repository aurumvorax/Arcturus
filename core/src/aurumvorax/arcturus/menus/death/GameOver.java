package aurumvorax.arcturus.menus.death;

import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOver extends MenuPage{
    private TextButton deadButton = new TextButton("You are dead.\nWalk it off.", Services.getSkin());
    private Table menuTable = new Table();

    public GameOver(){

        deadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Start);
            }
        });
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        menuTable.setFillParent(true);
        menuTable.add(deadButton);

        add(menuTable);
    }
}
