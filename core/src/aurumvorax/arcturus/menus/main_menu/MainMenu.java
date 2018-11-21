package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.services.Services;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.options.PreferenceManager;
import aurumvorax.arcturus.screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class MainMenu extends MenuState{

    private TextButton resumeButton = new TextButton("Resume", Services.MENUSKIN);
    private TextButton continueButton = new TextButton("Continue", Services.MENUSKIN);
    private TextButton newButton = new TextButton("New Game", Services.MENUSKIN);
    private TextButton loadButton = new TextButton("Load Game", Services.MENUSKIN);
    private TextButton saveButton = new TextButton("Save Game", Services.MENUSKIN);
    private TextButton optionsButton = new TextButton("Preferences", Services.MENUSKIN);
    private TextButton quitButton = new TextButton("Exit", Services.MENUSKIN);
    private Table menuTable = new Table();
    private Table outerTable = new Table();


    public MainMenu(){

        resumeButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                enterGame(Core.GameMode.Active);
            }
        });

        continueButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO - Load most recent save
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
                changeMenu(MenuScreen.MenuType.Load);
            }
        });

        saveButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuScreen.MenuType.Save);
            }
        });

        optionsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                changeMenu(MenuScreen.MenuType.Options);
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
    public Actor build(Stage menuStage){
        outerTable.reset();
        menuTable.reset();
        if(core.getGameMode() == Core.GameMode.Active)
            menuTable.add(resumeButton).row();
        else if(PreferenceManager.getLastSave() != null)
            menuTable.add(continueButton).row();
        menuTable.add(newButton).row();
        menuTable.add(saveButton).row();
        menuTable.add(loadButton).row();
        menuTable.add(optionsButton).row();
        menuTable.add(quitButton).row();

        Drawable menuBG = new NinePatchDrawable(Services.MENUSKIN.getPatch("list"));
        menuTable.setBackground(menuBG);

        outerTable.setFillParent(true);
        outerTable.add(menuTable.pad(10));
        return outerTable;
    }
}
