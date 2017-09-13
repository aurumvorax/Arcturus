package aurumvorax.arcturus;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

// The native LibGDX Json serializer does not support IntMaps, so we add IntMap handling here

public class IntMapSerializer implements Json.Serializer<IntMap>{


    @Override
    public void write(Json json, IntMap object, Class knownType){
        json.writeObjectStart();
        for(IntMap.Entry entry : (IntMap.Entries<?>) object.entries())
            json.writeValue(String.valueOf(entry.key), entry.value, null);
        json.writeObjectEnd();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IntMap read(Json json, JsonValue jsonData, Class type){
        IntMap intMap = new IntMap();
        for(JsonValue entry = jsonData.child; entry != null; entry = entry.next)
            intMap.put(Integer.parseInt(entry.name), json.readValue(entry.name, null, jsonData));
        return intMap;
    }
}
