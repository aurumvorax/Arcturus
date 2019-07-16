package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class MenuPage extends Table{


    private static final float FADETIME = 0.3f;
    private MenuFramework frame;


    protected MenuPage(MenuFramework frame){
        this.frame = frame;
    }

    public MenuPage show(){

        frame.setColor(1, 1, 1, 0);
        build();
        frame.addAction(Actions.fadeIn(FADETIME));

        return this;
    }

    protected abstract Actor build();

    protected void changeBack(){
        addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.changeBack())));
    }

    protected void changeMenu(MenuFramework.Page next){
        addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.changePage(next))));
    }

    protected void transition(Core.GameMode mode){
        addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.transition(mode))));
    }
}
