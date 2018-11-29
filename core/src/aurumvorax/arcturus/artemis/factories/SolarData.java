package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SolarData{

    Vector2 systemCoords;
    String starName;
    String starImgName;
    Vector2 starImgCenter;
    Array<OrbitalData> children;


    static class OrbitalData{
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
