package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.menus.Menu;
import aurumvorax.arcturus.menus.MenuState;
import aurumvorax.arcturus.options.Preferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class MainMenu extends MenuState{

    private static MainMenu INSTANCE = new MainMenu();
    public static MainMenu getInstance(){ return INSTANCE; }

    private TextButton resumeButton = new TextButton("Resume", Services.MENUSKIN);
    private TextButton continueButton = new TextButton("Continue", Services.MENUSKIN);
    private TextButton newButton = new TextButton("New Game", Services.MENUSKIN);
    private TextButton loadButton = new TextButton("Load Game", Services.MENUSKIN);
    private TextButton saveButton = new TextButton("Save Game", Services.MENUSKIN);
    private TextButton optionsButton = new TextButton("Preferences", Services.MENUSKIN);
    private TextButton quitButton = new TextButton("Exit", Services.MENUSKIN);
    private Table outerTable = new Table();
    private Table menuTable = new Table();


    private MainMenu(){

        resumeButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                master.exitTo(Core.ScreenType.Game);
            }
        });

        continueButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Most recent save is the active one - stored in prefs
                master.exitTo(Core.ScreenType.Game);
            }
        });

        newButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                Core.setActive(false);
                master.exitTo(Core.ScreenType.Game);
            }
        });

        loadButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                master.changeMenu(SaveLoad.getInstance(SaveLoad.Mode.LOAD));
            }
        });

        saveButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                master.changeMenu(SaveLoad.getInstance(SaveLoad.Mode.SAVE));
            }
        });

        optionsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
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
    public void enter(Menu entity){
        super.enter(entity);

        Drawable menuBG = new NinePatchDrawable(Services.MENUSKIN.getPatch("list"));

        outerTable.setFillParent(true);
        //menuTable.setDebug(true);
        if(Core.getActive())
            menuTable.add(resumeButton).row();
        else if(Preferences.lastSave != null)
            menuTable.add(continueButton).row();
        menuTable.add(newButton).row();
        menuTable.add(saveButton).row();
        menuTable.add(loadButton).row();
        menuTable.add(optionsButton).row();
        menuTable.add(quitButton).row();

        menuTable.setBackground(menuBG);

        outerTable.add(menuTable.pad(10));
        entity.getStage().addActor(outerTable);
    }

    @Override
    public void exit(Menu entity){
        outerTable.reset();
        menuTable.reset();
    }
}
