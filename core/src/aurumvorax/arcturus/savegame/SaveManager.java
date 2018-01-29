package aurumvorax.arcturus.savegame;

import aurumvorax.arcturus.Services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.HashMap;

public class SaveManager extends SaveSubject{

    private static class Singleton{ static final SaveManager INSTANCE = new SaveManager(); }
    public static SaveManager getInstance(){ return Singleton.INSTANCE; }

    private HashMap<String, FileHandle> allSaves;
    private ObjectMap<String, Object> thisSave = new ObjectMap<>();
    private Kryo kryo = new Kryo();

    private static final String SAVEGAME_SUFFIX = ".sav";

    private SaveManager() {
        ArrayKryoSerializer.registerArraySerializer(kryo);
        allSaves = new HashMap<>();
        readAllSaves();
    }

    public void saveElement(String key, Object object){
        thisSave.put(key, object);
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T> T loadElement(String key, Class<T> type){
        if( !thisSave.containsKey(key) ){
            return null;
        }
        return (T)thisSave.get(key);
    }

    public boolean isValid(String saveName){
        return allSaves.containsKey(saveName);
    }

    public Array<String> getSaveList(){
        readAllSaves();
        Array<String> saveNames = new Array<>();
        for(String save : allSaves.keySet()){
            saveNames.add(save);
        }
        return saveNames;
    }

    private void readAllSaves(){
        allSaves.clear();
        try{
            FileHandle[] files = Gdx.files.local(Services.SAVE_PATH).list(SAVEGAME_SUFFIX);
            for(FileHandle file: files) {
                allSaves.put(file.nameWithoutExtension(), file);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean saveGame(String saveName, boolean overwrite){
        notify(this, SaveObserver.SaveEvent.SAVING);
        String fullFileName = Services.SAVE_PATH + saveName + SAVEGAME_SUFFIX;

        boolean exists = Gdx.files.local(fullFileName).exists();

        if(exists && !overwrite)
            return false;

        try{
            FileHandle file = Gdx.files.local(fullFileName);
            Output output = new Output(file.write(!overwrite));
            output.writeString(saveName);
            kryo.writeObjectOrNull(output, thisSave, thisSave.getClass());
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        thisSave.clear();
        return true;
    }

    @SuppressWarnings("unchecked")
    public void loadGame(String saveName){
        String fullFileName = Services.SAVE_PATH + saveName + SAVEGAME_SUFFIX;
        Input input;
        try {
            FileHandle file = Gdx.files.local(fullFileName);
            input = new Input(file.read());
            try{
                String name = input.readString();
                if(!name.equals(saveName))  // Basic check to make sure this is the right file
                    input.close();
                thisSave = kryo.readObjectOrNull(input, thisSave.getClass());
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        notify(this, SaveObserver.SaveEvent.LOADING);
        thisSave.clear();
    }

    public void deleteGame(String saveName){
        String fullFileName = Services.SAVE_PATH + saveName + SAVEGAME_SUFFIX;
        try{
            Gdx.files.local(fullFileName).delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
