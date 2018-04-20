package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.render.WorldCam;
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
    // Destructor, PlayerInput TODO Docking
    public static void setTransition(MenuScreen.MenuType menu){
        transition = true;
        transitionTo = menu;
    }

    // Transitions into the Game world.  Called by MenuScreen.
    public static void resumeGame(){
        if(core.getGameMode() == Core.GameMode.Active){
            PlayerShip.insert();
        }
        core.switchScreen(Core.ScreenType.Game);
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
