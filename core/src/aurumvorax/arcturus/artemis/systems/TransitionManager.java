package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import com.artemis.BaseSystem;

public class TransitionManager extends BaseSystem{

    // Should be put first in the Artemis system load order, to ensure that no transitions are made
    // in the middle of a world cycle.

    private static Core core;
    private static boolean transition;
    private static Core.ScreenType transitionTo = null;
    private static boolean newGame = false;


    public TransitionManager(Core core){
        TransitionManager.core = core;
    }

    public static void setTransition(Core.ScreenType screen){
        transition = true;
        transitionTo = screen;
    }

    @Override
    protected void processSystem(){
        if(transition){
            transition = false;
            core.switchScreen(transitionTo);
        }
    }



}
