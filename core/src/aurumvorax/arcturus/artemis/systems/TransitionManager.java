package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.Destructor;
import aurumvorax.arcturus.screens.MenuScreen;
import com.artemis.BaseSystem;

public class TransitionManager extends BaseSystem{

    // Should be put first in the Artemis system load order, to ensure that no transitions are made
    // in the middle of a world cycle.

    private static Core core;
    private static boolean transition;
    private static float timer = 0;
    private static MenuScreen.MenuType transitionTo = null;


    public TransitionManager(Core core){
        TransitionManager.core = core;
    }

    // Queues a transition out of the Game world into a menu.  Called from within the Artemis world by
    // Destructor(Death), Menu(Exit) or TODO Docking

    public static void setTransition(MenuScreen.MenuType menu){
        if(timer == 0){
            transition = true;
            transitionTo = menu;
        }
    }

    // Transitions into the Game world.  Called by MenuFramework.
    public static void resumeGame(Core.GameMode mode){
        core.setGameMode(mode);

        switch(mode){
            case Active:
                PlayerShip.insert();

            case New:
                core.switchScreen(Core.ScreenType.Game);
                break;

            case Initial:
                core.switchScreen(Core.ScreenType.Menu);
                break;

            default:
                throw new IllegalStateException("TransitionManager should never be called with mode - " + mode);
        }
    }

    @Override
    protected void processSystem(){
        if(transition){
            if(transitionTo == MenuScreen.MenuType.Dead){
                transition = false;
                timer = 3f;
            }else{
                transition = false;
                PlayerShip.extract();
                Destructor.safeRemove(PlayerShip.getID());
                core.setMenuMode(transitionTo);
                core.switchScreen(Core.ScreenType.Menu);
            }
        }

        if(timer > 0)
            timer -= world.getDelta();

        if(timer < 0){
            timer = 0;
            core.setMenuMode(MenuScreen.MenuType.Dead);
            core.setGameMode(Core.GameMode.Dead);
            core.switchScreen(Core.ScreenType.Menu);
        }
    }
}
