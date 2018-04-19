package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.screens.MenuScreen;
import com.artemis.BaseSystem;

public class TransitionManager extends BaseSystem{

    // Should be put first in the Artemis system load order, to ensure that no transitions are made
    // in the middle of a world cycle.

    private static Core core;
    private static boolean transition;
    private static float timer = 0;
    private static MenuScreen.MenuType destination = null;

    //MenuScreen.setBackground(ScreenUtils.getFrameBufferTexture(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


    public TransitionManager(Core core){
        TransitionManager.core = core;
    }

    // Queues a transition out of the Game world into a menu.  Called from artemis systems.
    public static void setTransition(MenuScreen.MenuType menu){
        transition = true;
        destination = menu;
    }

    // Transitions into the Game world.  Called by MenuScreen.
    public static void resumeGame(){
        if(core.getGameMode() != Core.GameMode.New){
            PlayerShip.insert();
            core.setGameMode(Core.GameMode.Active);
        }
        core.switchScreen(Core.ScreenType.Game);
    }

    @Override
    protected void processSystem(){
        if(transition){
            if(destination != MenuScreen.MenuType.Dead){
                transition = false;
                PlayerShip.extract();
                core.setMenuMode(destination);
                core.switchScreen(Core.ScreenType.Menu);
            }else{
                timer = 3f;
                transition = false;
            }
        }

        if(timer > 0)
            timer -= world.getDelta();

        if(timer < 0){
            timer = 0;
            core.setMenuMode(MenuScreen.MenuType.Dead);
            core.switchScreen(Core.ScreenType.Menu);
        }
    }
}
