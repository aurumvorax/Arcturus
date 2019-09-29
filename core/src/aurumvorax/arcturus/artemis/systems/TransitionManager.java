package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.menus.MenuFramework;
import com.artemis.BaseSystem;

public class TransitionManager extends BaseSystem{

    // Should be put first in the Artemis system load order, to ensure that no transitions are made
    // in the middle of a world cycle.

    private static Core core;
    private static boolean transition;
    private static float timer = 0;
    private static MenuFramework.Page transitionTo = null;


    public TransitionManager(Core core){
        TransitionManager.core = core;
    }

    public static void pause(){ core.setGameMode(Core.GameMode.Paused); }
    public static void unpause(){ core.setGameMode(Core.GameMode.Active); }
    public static void togglePause(){ core.setGameMode((core.getGameMode() == Core.GameMode.Paused ? Core.GameMode.Active : Core.GameMode.Paused)); }

    // Queues a enterGame out of the Game world into a menu.  Called from within the Artemis world by
    // Destructor(Death), TODO Docking, or MenuFramework via enter
    public static void setTransition(MenuFramework.Page menu){
        if(timer == 0){
            transition = true;
            transitionTo = menu;
        }
    }

    // Transitions into the Game world.  Called by MenuFramework.
    public static void enterGame(Core.GameMode mode){
        core.setGameMode(mode);

        switch(mode){
            case Active:
                PlayerShip.insert();
                core.switchScreen(Core.ScreenType.Game);
                break;

            case New:
                core.switchScreen(Core.ScreenType.Game);
                break;

            case Start:
                setTransition(MenuFramework.Page.Start);
                break;

            default:
                throw new IllegalStateException("Invalid mode - " + mode);
        }
    }

    @Override
    protected void processSystem(){
        if(transition){
            if(transitionTo == MenuFramework.Page.Dead){
                transition = false;
                timer = 3f;
            }else{
                transition = false;
                PlayerShip.extract();
                Destructor.safeRemove(PlayerShip.getID());
                core.switchScreen(Core.ScreenType.Menu);
            }
        }

        if(timer > 0)
            timer -= world.getDelta();

        if(timer < 0){
            timer = 0;
            core.setGameMode(Core.GameMode.Dead);
            core.switchScreen(Core.ScreenType.Menu);
        }
    }
}
