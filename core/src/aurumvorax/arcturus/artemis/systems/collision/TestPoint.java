package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.CollisionPolygon;
import aurumvorax.arcturus.artemis.components.CollisionRadius;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class TestPoint{

    private static ComponentMapper<Physics2D> mPosition;
    private static ComponentMapper<CollisionRadius> mCollidable;
    private static ComponentMapper<CollisionPolygon> mPolygon;

    private static Vector2 axis = new Vector2();

    static boolean testC(int entity, Vector2 point){
        if(!mCollidable.has(entity))
            return false;               // Not collidable
        int radius = mCollidable.get(entity).radius;
        if(mPosition.get(entity).p.dst2(point) > radius * radius)
            return false;               // Outside collision radius
        return true;
    }

    static int testPolys(Vector2 point, IntBag entities){
        boolean miss;
        for(int e : entities.getData()){
            if(mPolygon.has(e)){
                for(Array<Vector2> vertices : mPolygon.get(e).getVertices(mPosition.get(e))){
                    miss = false;
                    for(int i = 0; i < vertices.size; i++){
                        axis.set(vertices.get(i)).sub(vertices.get((i + 1 >= vertices.size) ? 0 : i + 1));
                        axis.rotate90(-1).nor();
                        if(vertices.get(i).dot(axis) <= point.dot(axis)){
                            miss = true;
                            break;
                        }
                    }
                    if(!miss)
                        return e;
                }
            }
        }
        return -1;
    }
}
