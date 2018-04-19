package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class MenuState{


    private static final float FADE = 0.3f;
    protected MenuScreen screen;
    protected Core core;
    private Stage stage;
    private Actor root;

    public MenuState(Core core, MenuScreen screen, Stage stage){
        this.core = core;
        this.screen = screen;
        this.stage = stage;
    }

    public Actor enter(){
        root = build(stage);
        root.getColor().a = 0;
        root.addAction(Actions.fadeIn(FADE));
        return root;
    }

    protected abstract Actor build(Stage menuStage);

    protected void changeBack(){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.run(() -> screen.changeBack())));
    }

    protected void changeMenu(MenuState next){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.run(() -> screen.changeMenu(next))));
    }

    protected void enterGame(){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.run(() -> screen.enterGame())));
    }
}
