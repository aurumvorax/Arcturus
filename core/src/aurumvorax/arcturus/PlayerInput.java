package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.systems.PlayerControl;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.artemis.systems.render.WorldCam;
import aurumvorax.arcturus.options.Keys;
import aurumvorax.arcturus.screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

public class PlayerInput extends InputAdapter{

    private PlayerControl player;
    private WorldCam cam;

    private static final float SCROLLRATE = 0.1f;

    public PlayerInput(PlayerControl player, WorldCam cam){
        this.player = player;
        this.cam = cam;
    }

    @Override
    public boolean keyDown(int keycode){
        Keys.Command key = Services.keys.getCommand(keycode);
        if(key == null)
            return false;
        switch(key){
            case MENU:
                TransitionManager.setTransition(MenuScreen.MenuType.Main);
                break;
            case DOCK:
                TransitionManager.setTransition(MenuScreen.MenuType.Dock);
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
                player.controlHelm(0);
                break;
            case TURN_RIGHT:
                player.controlHelm(0);
                break;
            case FORWARD:
                player.controlThrust(0);
                break;
            case BACK:
                player.controlThrust(0);
                break;
            case STRAFE_LEFT:
                player.controlStrafe(0);
                break;
            case STRAFE_RIGHT:
                player.controlStrafe(0);
                break;
            case BRAKE:
                player.controlBrake(false);
                break;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y){
        cam.setMouse(x, y);
        player.setMouse(x, y);
        return true;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button){
        switch(button){
            case Input.Buttons.LEFT:
                player.controlFire(true);
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button){
        switch(button){
            case Input.Buttons.LEFT:
                player.controlFire(false);
                break;
            case Input.Buttons.RIGHT:
                player.selectTarget(x, y);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer){
        cam.setMouse(x, y);
        player.setMouse(x, y);
        return true;
    }

    @Override
    public boolean scrolled(int amount){
        cam.zoom(amount * SCROLLRATE);
        return false;
    }
}
