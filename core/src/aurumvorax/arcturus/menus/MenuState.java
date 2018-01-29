package aurumvorax.arcturus.menus;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public abstract class MenuState implements State<Menu>{

    // The states are used to setup the Stage held by Menu, in their respective enter() methods.  This includes
    // all the listeners, etc.  The effective state is a combination of the current Stage setup, and the state itself.

    protected Menu master;

    @Override
    public void enter(Menu entity){
        master = entity;
    }

    @Override
    public void exit(Menu entity){
        entity.getStage().clear();
    }

    @Override public void update(Menu entity){}
    @Override public boolean onMessage(Menu entity, Telegram telegram){ return false; }
}
