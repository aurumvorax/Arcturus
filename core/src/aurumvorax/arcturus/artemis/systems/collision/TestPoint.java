package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.CollisionPolygon;
import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class TestPoint{

    private static ComponentMapper<Physics2D> mPosition;
    private static ComponentMapper<CollisionRadius> mCollidable;
    private static ComponentMapper<CollisionPolygon> mPolygon;

    private static Vector2 axis = new Vector2();

    static boolean test(int entity, Vector2 point){
        if(!mCollidable.has(entity))
            return false;               // Not collidable
        int radius = mCollidable.get(entity).radius;
        if(mPosition.get(entity).p.dst2(point) > radius * radius)
            return false;               // Outside collision radius
        if(!mPolygon.has(entity))
            return true;                // Within radius, and circular collision type
        for(Array<Vector2> vertices : mPolygon.get(entity).getVertices(mPosition.get(entity))){
            for(int i = 0; i < vertices.size; i++){
                axis.set(vertices.get(i)).sub(vertices.get((i + 1 >= vertices.size) ? 0 : i + 1));
                axis.rotate90(-1).nor();
                if(vertices.get(i).dot(axis) <= point.dot(axis))
                    return false;
            }
        }
        return true;
    }
}
