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

    //Only for missiles

    float engineDuration;
    float turnRate;

    public enum Type{
        BULLET, MISSILE
    }
}

