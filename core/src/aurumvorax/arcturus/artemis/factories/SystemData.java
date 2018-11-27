package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SystemData{

    public Vector2 systemCoords;
    public String starName;
    public String starImgName;
    public Vector2 starImgCenter;
    public Array<OrbitalData> children;


    public static class OrbitalData{
        String name;
        String imgName;
        Vector2 imgCenter;
        float semimajor;
        float eccentricity;
        float offset;
        float tilt;
        double sweep;
        boolean dock;
        Array<OrbitalData> children;
    }
}
