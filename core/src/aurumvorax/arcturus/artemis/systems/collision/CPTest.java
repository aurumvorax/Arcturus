package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.CollisionPolygon;
import aurumvorax.arcturus.artemis.components.CollisionSimple;
import aurumvorax.arcturus.artemis.components.Position;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CPTest{

    private static Vector2 axis = new Vector2();
    private static Vector2 contact = new Vector2();
    private static Vector2 normal = new Vector2();

    private static ComponentMapper<Position> mPosition;
    private static ComponentMapper<CollisionSimple> mCollidable;
    private static ComponentMapper<CollisionPolygon> mPolygon;

    public static void test(int entityA, int entityB, Collision.Manifold m){
        m.penetration[0] = -Float.MAX_VALUE;
        for(Array<Vector2> polygon : mPolygon.get(entityB).getVertices(mPosition.get(entityB))){
            float poly = solvePoly(entityA, polygon);
            if(poly != 0 && poly > m.penetration[0]){
                m.penetration[0] = poly;
                m.normal.set(normal);
            }
        }
        if(m.penetration[0] > 0) {
            contact.set(m.normal).setLength(mCollidable.get(entityA).radius - m.penetration[0]).add(mPosition.get(entityA).p);
            m.contactPoints.add(contact);
            m.contacts = 1;
        }
    }

    private static float solvePoly(int entityA, Array<Vector2> verticesB){
        float minPenetration = Float.MAX_VALUE;
        Vector2 positionA = mPosition.get(entityA).p;
        float radiusA = mCollidable.get(entityA).radius;
        for(int i =0; i < verticesB.size; i++){
            float penetration;
            if(positionA.dst2(verticesB.get(i)) < radiusA * radiusA) {	// Corner Voronoi region
                penetration = radiusA - positionA.dst(verticesB.get(i));
                axis.set(verticesB.get(i)).sub(positionA);
            }else {																			// Edge Voronoi region
                axis.set(verticesB.get(i)).sub(verticesB.get((i + 1 >= verticesB.size) ? 0 : i + 1));
                axis.rotate90(-1).nor();
                penetration = verticesB.get(i).dot(axis) - positionA.dot(axis) + radiusA;
            }
            if(penetration < 0)
                return 0;
            if(penetration < minPenetration) {
                minPenetration = penetration;
                normal.set(axis);
            }
        }
        return minPenetration;
    }
}
