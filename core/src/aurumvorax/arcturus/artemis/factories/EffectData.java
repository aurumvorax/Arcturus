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
}
