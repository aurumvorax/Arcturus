package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.services.EntityData;
import com.badlogic.gdx.math.Vector2;

public class SolarFactory{

    private static String currentSystem;
    private static Vector2 currentSystemCoords;


    public static String getCurrentSystem(){ return currentSystem; }
    public static Vector2 getCurrentSystemCoords(){ return new Vector2(currentSystemCoords); }

    public static void createSystem(String systemName){
        SolarData data = EntityData.getSystemData(systemName);
        currentSystem = data.starName;
        currentSystemCoords = new Vector2(data.systemCoords);
        int starID = TerrainFactory.createStar(systemName, data.starImgName, data.starImgCenter);
        for(SolarData.OrbitalData child : data.children){
            buildChildren(child, starID);
        }
    }

    private static void buildChildren(SolarData.OrbitalData data, int parent){
        int thisOrbital = TerrainFactory.createOrbital(data, parent);
        if(data.children != null)
            for(SolarData.OrbitalData child : data.children)
                buildChildren(child, thisOrbital);
    }
}
