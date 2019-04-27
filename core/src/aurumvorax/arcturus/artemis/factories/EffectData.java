package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;

public class EffectData{

    //Common to all effect types
    public Type type;

    // Specific to Explosions
    String animName;
    Vector2 imgCenter;

    // Specific to TrailEffects
    public String textureName;
    public int segments;
    public float width;
    public float widen;

    //gunflash needs name and mount
    //engineglow needs name and mount

    public enum Type{
        EXPLOSION, TRAIL
    }

    public static boolean verify(EffectData data){
        if(data ==  null)
            return false;

        if(data.type == Type.EXPLOSION){
            return (data.animName != null &&
                    data.imgCenter != null);
        }

        if(data.type == Type.TRAIL){
            return (data.textureName != null &&
                    data.segments > 0 &&
                    data.width > 0);
        }

        return false;
    }
}
