package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Collidable;
import aurumvorax.arcturus.artemis.components.Position;
import com.artemis.ComponentMapper;

public enum BroadPhaseTest{
    INSTANCE;

    private static ComponentMapper<Position> mPosition;
    private static ComponentMapper<Collidable> mCollidable;

    public static boolean test(int entityA, int entityB){
        if(entityA == entityB)
            return false;       // Objects can't collide with themselves
        float radii = mCollidable.get(entityA).radius + mCollidable.get(entityB).radius;
        return !(mPosition.get(entityA).p.dst2(mPosition.get(entityB).p) > radii * radii);
    }
}
