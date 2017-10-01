package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.preferences.Preferences;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.savegame.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter{

    private Core core;
    private Stage stage;
    private TextButton resumeButton;
    private TextButton continueButton;
    private TextButton newButton;
    private TextButton loadButton;
    private TextButton saveButton;
    private TextButton optionsButton;
    private TextButton quitButton;

    public MainMenuScreen(Core core){
        this.core = core;
        stage = new Stage(new ScreenViewport());

        resumeButton = new TextButton("Resume", Services.MENUSKIN);
        continueButton = new TextButton("Continue", Services.MENUSKIN);
        newButton = new TextButton("New Game", Services.MENUSKIN);
        loadButton = new TextButton("Load Game", Services.MENUSKIN);
        saveButton = new TextButton("Save Game", Services.MENUSKIN);
        optionsButton = new TextButton("Preferences", Services.MENUSKIN);
        quitButton = new TextButton("Exit", Services.MENUSKIN);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                core.switchScreen(Core.ScreenType.Game);
            }
        });
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Most recent save is the active one - stored in prefs
                core.switchScreen(Core.ScreenType.Game);
            }
        });
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                core.setActive(false);
                core.switchScreen(Core.ScreenType.Game);
            }
        });
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.getInstance().loadGame("Test");
                core.switchScreen(Core.ScreenType.Game);
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
    }

    @Override
    public void show(){
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        if(core.getActive())
            table.add(resumeButton).row();
        else if(Preferences.lastSave != null)
            table.add(continueButton).row();
        table.add(newButton).row();
        table.add(saveButton).row();
        table.add(loadButton).row();
        table.add(optionsButton).row();
        table.add(quitButton).row();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
