package aurumvorax.arcturus.galaxy;

import aurumvorax.arcturus.artemis.factories.OrbitalData;
import aurumvorax.arcturus.artemis.factories.ShipData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class StellarData{

    public static class Base{
        public Vector2 systemCoords;
        String starName;
        String starImgName;
        Vector2 starImgCenter;
        Array<OrbitalData> children;
    }

    public static class Extra{
        public Array<ShipData.Unique> ships = new Array<>();
    }

    public static boolean verify(StellarData.Base data){
        if(data.systemCoords != null ||
                data.starName != null ||
                data.starImgName != null ||
                data.starImgCenter != null){

            if(data.children == null)
                return true;

            for(OrbitalData child : data.children){
                if(!OrbitalData.verify(child))
                    return false;
            }

            return true;

        }else
            return false;
    }
}
