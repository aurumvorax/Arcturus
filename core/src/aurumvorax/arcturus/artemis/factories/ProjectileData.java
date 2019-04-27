package aurumvorax.arcturus.artemis.factories;

import com.badlogic.gdx.math.Vector2;

public class ProjectileData{

    //Common to all projectile types
    Type type;
    String imgName;
    Vector2 imgCenter;
    int collisionRadius;
    float duration;
    float speed;
    float damage;

    // Specific to missiles

    float engineDuration;
    float turnRate;
    float thrust;
    float maxV;

    // Specific to missiles for now - will be expanded to include motion trails for bullets, gauss weapons, etc

    String trailName;
    Vector2 trailOffset;

    public enum Type{
        BULLET, MISSILE
    }

    public static boolean verify(ProjectileData data){
        if(data == null)
            return false;

        return (data.imgName != null ||
                data.imgCenter != null ||
                data.collisionRadius > 0 ||
                data.duration != 0);
    }
}

