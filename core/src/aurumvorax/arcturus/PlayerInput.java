package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.systems.PlayerControl;
import aurumvorax.arcturus.artemis.systems.TransitionManager;
import aurumvorax.arcturus.artemis.systems.collision.Selection;
import aurumvorax.arcturus.artemis.systems.render.HUDRenderer;
import aurumvorax.arcturus.artemis.systems.render.WorldCam;
import aurumvorax.arcturus.galaxy.SolarSystemManager;
import aurumvorax.arcturus.menus.MenuFramework;
import aurumvorax.arcturus.menus.codex.Codex;
import aurumvorax.arcturus.options.Keys;
import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class PlayerInput extends InputAdapter{

    private Core core;
    private PlayerControl player;
    private WorldCam cam;
    private HUDRenderer hud;

    private static final float SCROLLRATE = 0.1f;


    public PlayerInput(Core core, PlayerControl player, WorldCam cam, HUDRenderer hud){
        this.core = core;
        this.player = player;
        this.cam = cam;
        this.hud = hud;
    }

    @Override
    public boolean keyDown(int keycode){
        Keys.Command key = Services.keys.getCommand(keycode);
        if(key == null)
            return false;
        switch(key){
            case MENU:
                TransitionManager.pause();
                hud.showMenu(MenuFramework.Page.Game);
                break;

            case PAUSE:
                TransitionManager.togglePause();
                break;

            case DOCK:
                int dock = Selection.getDock();
                if(dock > -1)
                    TransitionManager.setTransition(MenuFramework.Page.Shipyard);
                break;

            case MAP:
                hud.showCodex(Codex.Page.GalaxyMap);
                break;

            case JUMP:
                SolarSystemManager.hyperdrive();
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

            case Input.Buttons.RIGHT:
                player.selectTarget(x, y);
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
