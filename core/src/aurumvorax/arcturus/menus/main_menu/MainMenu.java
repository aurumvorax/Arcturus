package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.menus.Menu;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.options.Preferences;
import aurumvorax.arcturus.savegame.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends MenuState{
    private static MainMenu Instance = new MainMenu();

    public static MainMenu getInstance(){ return Instance; }

    private MainMenu(){}

    @Override
    public void enter(Menu entity){

        TextButton resumeButton = new TextButton("Resume", Services.MENUSKIN);
        TextButton continueButton = new TextButton("Continue", Services.MENUSKIN);
        TextButton newButton = new TextButton("New Game", Services.MENUSKIN);
        TextButton loadButton = new TextButton("Load Game", Services.MENUSKIN);
        TextButton saveButton = new TextButton("Save Game", Services.MENUSKIN);
        TextButton optionsButton = new TextButton("Preferences", Services.MENUSKIN);
        TextButton quitButton = new TextButton("Exit", Services.MENUSKIN);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entity.exitTo(Core.ScreenType.Game);
            }
        });
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Most recent save is the active one - stored in prefs
                entity.exitTo(Core.ScreenType.Game);
            }
        });
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Core.setActive(false);
                entity.exitTo(Core.ScreenType.Game);
            }
        });
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.getInstance().loadGame("Test");
                entity.exitTo(Core.ScreenType.Game);
            }
        });
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.getInstance().saveGame("Test");
            }
        });
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        if(Core.getActive())
            table.add(resumeButton).row();
        else if(Preferences.lastSave != null)
            table.add(continueButton).row();
        table.add(newButton).row();
        table.add(saveButton).row();
        table.add(loadButton).row();
        table.add(optionsButton).row();
        table.add(quitButton).row();

        entity.getStage().addActor(table);
    }
}
