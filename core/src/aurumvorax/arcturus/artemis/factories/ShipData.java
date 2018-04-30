package aurumvorax.arcturus.artemis.factories;

import aurumvorax.arcturus.artemis.components.shipComponents.Mount;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;

public  class ShipData{

    public String imgName;
    public Vector2 imgCenter;
    int collisionRadius;
    Array<Array<Vector2>> vertices;
    public Array<Mount.Weapon> weaponMounts;
    public float hull;
    public HashMap<String, Variant> variants;

    public static class Variant{
        public IntMap<String> weapons;
    }

}