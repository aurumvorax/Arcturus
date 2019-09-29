package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameMenu extends MenuPage{

    private TextButton resumeButton = new TextButton("Resume Game", Services.getSkin());
    private TextButton saveButton = new TextButton("Save Game", Services.getSkin());
    private TextButton loadButton = new TextButton("Load Game", Services.getSkin());
    private TextButton optionsButton = new TextButton("Options", Services.getSkin());
    private TextButton quitToMenuButton = new TextButton("Exit to Start Menu", Services.getSkin());
    private TextButton exitButton = new TextButton("Quit to Desktop", Services.getSkin());
    private Table menuTable = new Table();

    public GameMenu(){

        resumeButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                closeMenu();
            }
        });

        saveButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Save);
            }
        });

        loadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Load);
            }
        });

        optionsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Options);
            }
        });

        quitToMenuButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Start);
            }
        });

        exitButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Gdx.app.exit();
            }
        });
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        menuTable.add(resumeButton).row();
        menuTable.add(saveButton).row();
        menuTable.add(loadButton).row();
        menuTable.add(optionsButton).row();
        menuTable.row();
        menuTable.add(quitToMenuButton).row();
        menuTable.add(exitButton).row();

        add(menuTable.pad(10));

    }
}
