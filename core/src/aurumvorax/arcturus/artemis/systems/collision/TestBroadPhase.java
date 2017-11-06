package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.ComponentMapper;

class TestBroadPhase{

    private static ComponentMapper<Physics2D> mPosition;
    private static ComponentMapper<CollisionRadius> mCollidable;
    private static ComponentMapper<Beam> mBeam;

    static boolean test(int entityA, int entityB){
        if(entityA == entityB)
            return false;       // Objects can't collide with themselves

        if(mBeam.has(entityA))
            return TestBeam.broadPhase(entityB, entityA);
        if(mBeam.has(entityB))
            return TestBeam.broadPhase(entityA, entityB);

        float radii = mCollidable.get(entityA).radius + mCollidable.get(entityB).radius;
        return !(mPosition.get(entityA).p.dst2(mPosition.get(entityB).p) > radii * radii);
    }
}
