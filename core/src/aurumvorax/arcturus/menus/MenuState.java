package aurumvorax.arcturus.menus;

import aurumvorax.arcturus.Core;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class MenuState{

    // A MenuState is responsible for setting up a Scene2D.ui menu page.  Most of the setup will be done in the
    // constructor.  Context specific changes are made in the refresh() method.  Enter() will call refresh(), and
    // then return the root actor for the page.  The menu page can be exited via a listener call to one of the
    // following methods: changeBack(), changeMenu(), or exitTo()

    private static final float FADE = 0.3f;
    protected Menu menu;
    protected Actor root;

    public Actor enter(Menu menu, Stage menuStage){
        this.menu = menu;
        root = build(menuStage);
        root.getColor().a = 0;
        root.addAction(Actions.fadeIn(FADE));
        return root;
    }

    protected abstract Actor build(Stage menuStage);

    protected void changeBack(){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.alpha(1), Actions.run(() -> menu.changeBack())));
    }

    protected void changeMenu(MenuState next){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.alpha(1), Actions.run(() -> menu.changeMenu(next))));
    }

    protected void exitTo(Core.ScreenType screen){
        root.addAction(Actions.sequence(Actions.fadeOut(FADE), Actions.alpha(1), Actions.run(() -> menu.exitTo(screen))));
    }
}
