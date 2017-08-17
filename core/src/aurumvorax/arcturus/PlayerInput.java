package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.systems.PlayerControl;
import aurumvorax.arcturus.artemis.systems.WorldCam;
import com.badlogic.gdx.InputAdapter;

public class PlayerInput extends InputAdapter{

    private Core core;
    private PlayerControl player;
    private WorldCam cam;

    private static final float SCROLLRATE = 0.1f;

    public PlayerInput(Core core, PlayerControl player, WorldCam cam){
        this.core = core;
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
        cam.setMouse(x, y);
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
        cam.setMouse(x, y);
        return true;
    }

    @Override
    public boolean scrolled(int amount){
        cam.Zoom(amount * SCROLLRATE);
        return false;
    }
}