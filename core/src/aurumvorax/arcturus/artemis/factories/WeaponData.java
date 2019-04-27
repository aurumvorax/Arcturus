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

    public static boolean verify(WeaponData data){
        if(data == null)
            return false;

        if(data.imgName == null ||
                data.imgCenter == null ||
                data.rotationSpeed == 0 ||
                data.barrels == null ||
                data.barrels.get(0) == null)
            return false;

        if(data.type == Type.CANNON || data.type == Type.LAUNCHER){
            return (data.launches != null);
        }

        if(data.type == Type.BEAM){
            return (data.beamImgName != null &&
                    data.beamImgCenter != null &&
                    data.range > 0);
        }

        return false;
    }
}
