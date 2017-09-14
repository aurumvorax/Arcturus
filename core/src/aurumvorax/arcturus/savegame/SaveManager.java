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

    private HashMap<String, FileHandle> allSaves;
    private ObjectMap<String, Object> thisSave = new ObjectMap<>();
    private Kryo kryo = new Kryo();

    private static final String SAVEGAME_SUFFIX = ".sav";

    private SaveManager() {
        ArrayKryoSerializer.registerArraySerializer(kryo);
        //kryo.register(SpriteRenderer.class);
        allSaves = new HashMap<>();
        //readAllSaves();
    }

    public Array<String> getSaveList(){
        Array<String> saveNames = new Array<>();
        for(String save : allSaves.keySet()){
            saveNames.add(save);
        }
        return saveNames;
    }

    public void readAllSaves(){
        try{
            FileHandle[] files = Gdx.files.local(Services.SAVE_PATH).list(SAVEGAME_SUFFIX);
            for(FileHandle file: files) {
                allSaves.put(file.nameWithoutExtension(), file);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveGame(String saveName){
        notify(this, SaveObserver.SaveEvent.SAVING);
        String fullFileName = Services.SAVE_PATH + saveName + SAVEGAME_SUFFIX;
        try{
            FileHandle file = Gdx.files.local(fullFileName);
            Output output = new Output(file.write(false));
            output.writeString(saveName);
            kryo.writeObjectOrNull(output, thisSave, thisSave.getClass());
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        thisSave.clear();
    }

    public void setProperty(String key, Object object){
        thisSave.put(key, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type){
        if( !thisSave.containsKey(key) ){
            return null;
        }
        return (T)thisSave.get(key);
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
}
