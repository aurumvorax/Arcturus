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
}
