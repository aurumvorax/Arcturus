package aurumvorax.arcturus.menus.main_menu;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.MenuPage;
import aurumvorax.arcturus.options.PreferenceManager;
import aurumvorax.arcturus.savegame.SaveManager;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class StartMenu extends MenuPage{

    private TextButton continueButton = new TextButton("Continue", Services.MENUSKIN);
    private TextButton newButton = new TextButton("New Game", Services.MENUSKIN);
    private TextButton loadButton = new TextButton("Load Game", Services.MENUSKIN);
    private TextButton saveButton = new TextButton("Save Game", Services.MENUSKIN);
    private TextButton optionsButton = new TextButton("Preferences", Services.MENUSKIN);
    private TextButton quitButton = new TextButton("Exit", Services.MENUSKIN);
    private Table menuTable = new Table();
    private Table outerTable = new Table();


    public StartMenu(MenuFramework frame){
        super(frame);

        continueButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.INSTANCE.loadGame(PreferenceManager.getLastSave());
                transition(Core.GameMode.Active);
            }
        });

        newButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                transition(Core.GameMode.New);
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
    protected Actor build(){
        outerTable.reset();
        menuTable.reset();

        if(PreferenceManager.getLastSave() != null)
            menuTable.add(continueButton).row();
        menuTable.add(newButton).row();
        menuTable.add(saveButton).row();
        menuTable.add(loadButton).row();
        menuTable.add(optionsButton).row();
        menuTable.row();
        menuTable.add(quitButton).row();

        Drawable menuBG = new NinePatchDrawable(Services.MENUSKIN.getPatch("list"));
        menuTable.setBackground(menuBG);

        outerTable.setFillParent(true);
        outerTable.add(menuTable.pad(10));

        return outerTable;
    }
}
