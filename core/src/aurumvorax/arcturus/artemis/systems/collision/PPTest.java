package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.CollisionPolygon;
import aurumvorax.arcturus.artemis.components.Position;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public enum PPTest{
    INSTANCE;

    private static Array<Array<Vector2>> polygonsA;
    private static Array<Array<Vector2>> polygonsB;
    private static Vector2 axis = new Vector2();
    private static Vector2 left = new Vector2();
    private static Vector2 right = new Vector2();
    private static Vector2 refV = new Vector2();
    private static Vector2 normal = new Vector2();
    private static Vector2 polyNormal = new Vector2();
    private static Vector2[] cp = {new Vector2(), new Vector2()};
    private static Vector2[] out = {new Vector2(), new Vector2()};
    private static Vector2[] edgeA = {new Vector2(), new Vector2()};
    private static Vector2[] edgeB = {new Vector2(), new Vector2()};

    private static ComponentMapper<CollisionPolygon> mPolygon;
    private static ComponentMapper<Position> mPosition;


    public static void test(int entityA, int entityB, Collision.Manifold m){
        polygonsA = mPolygon.get(entityA).getVertices(mPosition.get(entityA));
        polygonsB = mPolygon.get(entityB).getVertices(mPosition.get(entityB));
        int bestI = -1;
        int bestJ = -1;
        m.penetration[0] = -Float.MAX_VALUE;

        for(int i = 0; i < polygonsA.size; i++){
            for(int j = 0; j < polygonsB.size; j++){
                float poly = solvePoly(polygonsA.get(i), polygonsB.get(j));
                if((poly > 0) && (poly > m.penetration[0])){
                    m.normal.set(polyNormal);
                    m.penetration[0] = poly;
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        if(m.penetration[0] <= 0)
            return;

        boolean flip;
        // Because collision normal points from A to B, we invert it for finding the best edge of B, then invert it back
        findBestEdge(polygonsA.get(bestI), m.normal, edgeA);
        m.normal.scl(-1.0f);
        findBestEdge(polygonsB.get(bestJ), m.normal, edgeB);
        m.normal.scl(-1.0f);

        // Edge clipping to determine contact points and depths
        Vector2[] refEdge;
        if(Math.abs(left.set(edgeA[1]).sub(edgeA[0]).dot(m.normal)) <
                Math.abs(right.set(edgeB[1]).sub(edgeB[0]).dot(m.normal))){
            refEdge = edgeA;
            cp[0].set(edgeB[0]);
            cp[1].set(edgeB[1]);
            flip = false;
        }else{
            refEdge = edgeB;
            cp[0].set(edgeA[0]);
            cp[1].set(edgeA[1]);
            flip = true;
        }
        refV.set(refEdge[1]).sub(refEdge[0]).nor();
        float o1 = refV.dot(refEdge[0]);
        if (clip(refV, o1, cp) < 2)
            return;			// Floating point error - try again next update;
        float o2 = refV.dot(refEdge[1]);
        if(clip(refV.scl(-1.0f), -o2, cp) < 2)
            return;			// Floating point error - try again next update;
        m.normal.set(refV).rotate90(-1);
        float ref = m.normal.dot(refEdge[0]);
        m.penetration[0] = ref - m.normal.dot(cp[0]);
        m.penetration[1] = ref - m.normal.dot(cp[1]);
        if(m.penetration[0] >= 0.0f)
            m.contactPoints.add(cp[0]);
        else
            m.penetration[0] = m.penetration[1];
        if(m.penetration[1] >= 0.0f)
            m.contactPoints.add(cp[1]);
        if(flip) m.normal.scl(-1.0f);
        m.contacts = m.contactPoints.size;
    }

    private static float solvePoly(Array<Vector2> verticesA, Array<Vector2> verticesB){

        float penetrationA = findLeastPenetration(verticesA, verticesB);
        if(penetrationA <= 0f)  // Found separating axis
            return 0f;
        polyNormal.set(normal);
        float penetrationB = findLeastPenetration(verticesB, verticesA);
        if(penetrationB <= 0f)	// Found separating axis
            return 0f;
        if(penetrationA > penetrationB) {
            polyNormal.set(normal).scl(-1.0f);
            return penetrationB;
        }
        return penetrationA;
    }

    private static float findLeastPenetration(Array<Vector2> verticesA, Array<Vector2> verticesB){

        float leastPenetration = Float.MAX_VALUE;
        for(int i = 0; i < verticesA.size; i++){
            axis.set(verticesA.get(i)).sub(verticesA.get((i + 1 >= verticesA.size) ? 0 : i + 1));
            axis.rotate90(-1).nor();
            float projectionA = -Float.MAX_VALUE;
            float projectionB = Float.MAX_VALUE;
            for(int j = 0; j < verticesA.size; j++){
                float projection = verticesA.get(j).dot(axis);
                if(projection > projectionA)
                    projectionA = projection;
            }
            for(int j = 0; j < verticesB.size; j++){
                float projection = verticesB.get(j).dot(axis);
                if(projection < projectionB)
                    projectionB = projection;
            }
            float penetration = projectionA - projectionB;
            if(penetration <= 0)
                return penetration;
            if(penetration < leastPenetration){
                leastPenetration = penetration;
                normal.set(axis);
            }
        }
        return leastPenetration;
    }

    // Find and format the polygon's edge to be used in incident face clipping.  Returned edge format is
    // the two points that define the edge, in clockwise winding order.
    private static void findBestEdge(Array<Vector2> vertices, Vector2 normal, Vector2[] edge){
        float max = -Float.MAX_VALUE;
        int index = 0;
        for(int i = 0; i < vertices.size; i++){
            float projection = vertices.get(i).dot(normal);
            if(projection > max){
                max = projection;
                index = i;
            }
            left.set(vertices.get(index)).sub(vertices.get((index <= 0) ? vertices.size - 1 : index - 1)).nor();
            right.set(vertices.get(index)).sub(vertices.get((index + 1 >= vertices.size) ? 0 : index + 1)).nor();
            if(left.dot(normal) < right.dot(normal)){
                edge[0].set(vertices.get((index <= 0) ? vertices.size - 1 : index - 1));
                edge[1].set(vertices.get(index));
            }else{
                edge[0].set(vertices.get(index));
                edge[1].set(vertices.get((index + 1 >= vertices.size) ? 0 : index + 1));
            }
        }
    }

    // Used for clipping incident planes
    private static int clip(Vector2 n, float o, Vector2[] face){
        int sp = 0;
        float d1 = n.dot(face[0]) - o;
        float d2 = n.dot(face[1]) - o;
        if(d1 >= 0.0f) out[sp++].set(face[0]);
        if(d2 >= 0.0f) out[sp++].set(face[1]);
        if(d1 * d2 < 0.0f){
            float u = d1 / (d1 - d2);
            out[sp++].set(face[1]).sub(face[0]).scl(u).add(face[0]);
        }
        face[0].set(out[0]);
        face[1].set(out[1]);
        assert(sp <= 2);
        return sp;
    }
}
