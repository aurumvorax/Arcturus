package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class WeaponData{

    // Generic to all weapons
    public Type type;
    public String imgName;
    public Vector2 imgCenter;
    float rotationSpeed;
    Array<Vector2> barrels;

    // Specific to Cannons and Launchers
    String launches;
    float delay;
    float reload;

    // Specific to Beams
    String beamImgName;
    Vector2 beamImgCenter;
    float range;
    float dps;

    public enum Type{
        CANNON, LAUNCHER, BEAM
    }
}
