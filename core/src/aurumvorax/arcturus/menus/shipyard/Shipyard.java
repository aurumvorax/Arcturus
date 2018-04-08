package aurumvorax.arcturus.menus.shipyard;

import aurumvorax.arcturus.menus.MenuState;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Shipyard extends MenuState{

    private static Shipyard INSTANCE = new Shipyard();
    public static Shipyard getInstance(){ return INSTANCE; }

    @Override
    protected Actor build(Stage menuStage){
        return null;
    }
}
