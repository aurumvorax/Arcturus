package aurumvorax.arcturus.options;

import aurumvorax.arcturus.services.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntMap;

public class Keys{

    public Keys(){
        if(!loadKeybinds()) {
            setDefaults();
            saveKeybinds();
        }
    }

    private static IntMap<Command> keybinds = new IntMap<>();

    public Command getCommand(int key){ return keybinds.get(key); }
    public IntMap getAllCommands(){ return keybinds; }
    public void setCommand(int key, Command command){ keybinds.put(key, command); }

    public void setDefaults(){
        keybinds.put(Input.Keys.ESCAPE, Command.MENU);
        keybinds.put(Input.Keys.W, Command.FORWARD);
        keybinds.put(Input.Keys.A, Command.TURN_LEFT);
        keybinds.put(Input.Keys.S, Command.BACK);
        keybinds.put(Input.Keys.D, Command.TURN_RIGHT);
        keybinds.put(Input.Keys.Q, Command.STRAFE_LEFT);
        keybinds.put(Input.Keys.E, Command.STRAFE_RIGHT);
        keybinds.put(Input.Keys.C, Command.BRAKE);
        keybinds.put(Input.Keys.T, Command.DOCK);
        keybinds.put(Input.Keys.M, Command.MAP);
        keybinds.put(Input.Keys.J, Command.JUMP);
        keybinds.put(Input.Keys.P, Command.PAUSE);
    }

    public boolean saveKeybinds(){
        if(Gdx.files.isLocalStorageAvailable()) {
            FileHandle file = Gdx.files.local(Services.KEY_PATH);
            file.writeString(Services.json.prettyPrint(Services.json.toJson(keybinds)), false);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean loadKeybinds(){
        if(Gdx.files.isLocalStorageAvailable() && Gdx.files.local(Services.KEY_PATH).exists()){
            keybinds.clear();
            keybinds = Services.json.fromJson(IntMap.class, Gdx.files.local(Services.KEY_PATH));
            return true;
        }
        return false;
    }

    public enum Command{
        MENU, FORWARD, BACK, TURN_LEFT, TURN_RIGHT, STRAFE_LEFT, STRAFE_RIGHT, BRAKE, DOCK, MAP, JUMP, PAUSE
    }
}