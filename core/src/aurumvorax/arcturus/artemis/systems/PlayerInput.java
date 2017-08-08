package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Keys;
import aurumvorax.arcturus.Services;
import com.artemis.BaseSystem;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class PlayerInput extends InputAdapter{

    private Core core;
    private PlayerControl player;

    public PlayerInput(Core core, PlayerControl player){
        this.core = core;
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode){
        Keys.Command key = Services.keys.getCommand(keycode);
        if(key == null)
            return false;
        switch(key){
            case MENU:
                core.switchScreen(Core.ScreenType.MainMenu);
                break;
            case TURN_LEFT:
                player.controlHelm(1);
                break;
            case TURN_RIGHT:
                player.controlHelm(-1);
                break;
            case FORWARD:
                player.controlThrust(1);
                break;
            case BACK:
                player.controlThrust(-1);
                break;
            case STRAFE_LEFT:
                player.controlStrafe(1);
                break;
            case STRAFE_RIGHT:
                player.controlStrafe( -1);
                break;
            case BRAKE:
                player.controlBrake(true);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        Keys.Command key = Services.keys.getCommand(keycode);
        if(key == null)
            return false;
        switch(key){
            case TURN_LEFT:
                player.controlHelm(-1);
                break;
            case TURN_RIGHT:
                player.controlHelm(1);
                break;
            case FORWARD:
                player.controlThrust(-1);
                break;
            case BACK:
                player.controlThrust(1);
                break;
            case STRAFE_LEFT:
                player.controlStrafe(-1);
                break;
            case STRAFE_RIGHT:
                player.controlStrafe(1);
                break;
            case BRAKE:
                player.controlBrake(false);
                break;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y){
        return true;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button){
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button){
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer){
        return true;
    }

    @Override
    public boolean scrolled(int amount){
        return false;
    }
}
