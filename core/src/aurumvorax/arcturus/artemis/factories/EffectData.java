package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;

public class EffectData{

    //Common to all effect types
    Type type;

    // Specific to Explosions
    String animName;
    Vector2 imgCenter;

    //explosion just needs name and physics
    //gunflash needs name and mount
    //engineglow needs name and mount
    //smoketrail needs name and location

    public enum Type{
        EXPLOSION, PARTICLE
    }
}
