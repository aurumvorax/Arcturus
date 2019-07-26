package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class MenuPage extends Table{


    private static final float FADETIME = 0.2f;
    private MenuFramework frame;

    private TextureRegion background;
    private Vector2 size;


    public MenuPage show(MenuFramework frame){
        this.frame = frame;
        setColor(1,1,1,1);
        frame.setColor(1, 1, 1, 0);
        build();
        frame.addAction(Actions.fadeIn(FADETIME));

        return this;
    }

    protected abstract void build();

    protected void changeBack(){
        frame.addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.changeBack())));
    }

    protected void changeMenu(MenuFramework.Page next){
        frame.addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.changePage(next))));
    }

    protected void enterGame(Core.GameMode mode){
        frame.addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.enterGame(mode))));
    }

    protected void closeMenu(){
        frame.addAction(Actions.sequence(Actions.fadeOut(FADETIME), Actions.run(() -> frame.closeMenu())));
    }
}
