package aurumvorax.arcturus.artemis.systems.collision;

// This is the collision handler for actor/projectile collisions.  The projectile is destroyed, and the actor
// takes damage as appropriate.

import aurumvorax.arcturus.artemis.components.Ephemeral;
import aurumvorax.arcturus.artemis.components.Projectile;
import aurumvorax.arcturus.artemis.systems.Damage;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class BoomHandler implements CollisionHandler{

    private static Vector2 impact = new Vector2();

    private static ComponentMapper<Projectile> mProjectile;

    @Override
    public void onCollide(int actor, int projectile, Collision.Manifold m){
        if(mProjectile.get(projectile).firedFrom == actor)
            return;                                         // Can't shoot own ship

        if(m.contacts == 2)
            impact.set(m.contactPoints.get(0)).add(m.contactPoints.get(1)).scl(0.5f);
        else
            impact.set(m.contactPoints.get(0));

        Damage.hull(actor, impact, mProjectile.get(projectile).damage);
        Damage.destroyEntity(projectile);
    }
}
