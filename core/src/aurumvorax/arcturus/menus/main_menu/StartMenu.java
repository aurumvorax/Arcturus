package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class StartMenu extends MenuPage{

    private TextButton continueButton = new TextButton("Continue", Services.getSkin());
    private TextButton newButton = new TextButton("New Game", Services.getSkin());
    private TextButton loadButton = new TextButton("Load Game", Services.getSkin());
    private TextButton saveButton = new TextButton("Save Game", Services.getSkin());
    private TextButton optionsButton = new TextButton("Options", Services.getSkin());
    private TextButton quitButton = new TextButton("Exit", Services.getSkin());
    private Table menuTable = new Table();


    public StartMenu(){

        continueButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.INSTANCE.loadGame(Services.prefs.getLastSave());
                enterGame(Core.GameMode.Active);
            }
        });

        newButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.New);
            }
        });

        loadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Load);
            }
        });

        saveButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Save);
            }
        });

        optionsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuFramework.Page.Options);
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    protected void build(){
        reset();
        menuTable.reset();

        if(Services.prefs.getLastSave() != null)
            menuTable.add(continueButton).row();
        menuTable.add(newButton).row();
        menuTable.add(saveButton).row();
        menuTable.add(loadButton).row();
        menuTable.add(optionsButton).padBottom(30).row();
        menuTable.add(quitButton).row();

        menuTable.setFillParent(true);

        add(menuTable);
    }
}
