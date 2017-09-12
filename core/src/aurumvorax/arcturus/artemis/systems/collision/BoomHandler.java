package aurumvorax.arcturus.artemis.systems.collision;

// This is the collision handler for actor/projectile collisions.  The projectile is destroyed, and the actor
// takes damage as appropriate.

import aurumvorax.arcturus.artemis.components.Ephemeral;
import aurumvorax.arcturus.artemis.components.Projectile;
import com.artemis.ComponentMapper;

public class BoomHandler implements CollisionHandler{

    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Ephemeral> mEphemeral;


    @Override
    public void onCollide(int actor, int projectile, Collision.Manifold m){
        if(mProjectile.get(projectile).firedFrom == actor)
            return;                                         // Can't shoot own ship
        mEphemeral.get(projectile).lifespan = 0;
    }
}
