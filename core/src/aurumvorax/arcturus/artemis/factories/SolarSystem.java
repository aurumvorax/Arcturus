package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.services.EntityData;
import com.badlogic.gdx.math.Vector2;

public class SolarSystem{

    private static String currentSystem;
    private static Vector2 currentSystemCoords;


    public static String getCurrentSystem(){ return currentSystem; }
    public static Vector2 getCurrentSystemCoords(){ return new Vector2(currentSystemCoords); }

    public static void createSystem(String systemName){
        SystemData data = EntityData.getSystemData(systemName);
        currentSystem = data.starName;
        currentSystemCoords = new Vector2(data.systemCoords);
        int starID = TerrainFactory.createStar(systemName, data.starImgName, data.starImgCenter);
        for(SystemData.OrbitalData child : data.children){
            buildChildren(child, starID);
        }
    }

    private static void buildChildren(SystemData.OrbitalData data, int parent){
        int thisOrbital = TerrainFactory.createOrbital(data, parent);
        if(data.children != null)
            for(SystemData.OrbitalData child : data.children)
                buildChildren(child, thisOrbital);
    }
}
