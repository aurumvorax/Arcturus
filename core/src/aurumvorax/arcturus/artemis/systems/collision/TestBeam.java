package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class TestBeam{

    private static Vector2 beam = new Vector2();
    private static Vector2 line = new Vector2();
    private static Vector2 start = new Vector2();
    private static Vector2 offset = new Vector2();
    private static Vector2 contact = new Vector2();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<CollisionPolygon> mPolygon;

    static boolean broadPhase(int entityID, int beamID){
        if(!mBeam.get(beamID).active)   // Is the beam turned on?
            return false;
        if(mMounted.get(beamID).parent == entityID)     // Can't shoot ourselves
            return false;

        float distance2 = mPhysics.get(entityID).p.dst2(mMounted.get(beamID).position);  // Range check
        float maxRange = mBeam.get(beamID).maxRange + mRadius.get(entityID).radius;
        return (distance2 <= (maxRange * maxRange));
    }

    static void test(int entityID, int beamID, Collision.Manifold m){
        Beam b = mBeam.get(beamID);
        if(!mPolygon.has(entityID)){    // Circle - Segment

            float radius = mRadius.get(entityID).radius;
            line.set(mPhysics.get(entityID).p).sub(b.origin);
            float cross = line.crs(b.unitBeam);
            if(Math.abs(cross) > radius)    // Miss to the side
                return;

            float dot = line.dot(b.unitBeam);
            if(dot <= 0)                          // Ignore targets behind turret
                return;

            m.penetration[0] = (float) Math.sqrt((radius * radius) - (cross * cross));
            if((dot - m.penetration[0]) > b.maxRange)     // Beam falls short
                return;

            if(b.length > dot - m.penetration[0]){
                b.length = dot - m.penetration[0];
                contact.set(b.unitBeam).scl(b.length).add(b.origin);
                m.contactPoints.add(contact);
                m.contacts = 1;

            }else
                m.reset();

        }else{       // Polygon - Segment
            beam.set(b.unitBeam).scl(b.maxRange);
            m.contacts = 0;
            float bestT = Float.MAX_VALUE;
            Vector2 bestOffset = new Vector2(0, 0);
            for(Array<Vector2> vertices : mPolygon.get(entityID).getVertices(mPhysics.get(entityID))){
                for(int i = 0; i < vertices.size; i++){
                    start.set(vertices.get(i));
                    line.set(vertices.get((i >= vertices.size - 1) ? 0 : i + 1)).sub(start);
                    float cross = beam.crs(line);
                    if(cross == 0)      //parallel or collinear
                        continue;

                    float invCross = 1 / cross;
                    offset.set(start).sub(b.origin);
                    float u = offset.crs(beam) * invCross;
                    if(u < 0 || u > 1)      // no intersect
                        continue;

                    float t = offset.crs(line) * invCross;
                    if(t < 0 || t > 1)      // no intersect
                        continue;

                    if(t < bestT){          //is this intersect the closest?
                        m.contacts = 1;
                        bestT = t;
                        bestOffset.set(offset);
                    }
                }
            }
            if(m.contacts == 0)         // no intersect found
                return;

            if(b.length > b.maxRange * bestT){
                b.length = (b.maxRange * bestT);
                m.penetration[0] = b.maxRange - b.length;
                contact.set(beam).scl(bestT).add(b.origin);
                m.contactPoints.add(contact);

            }else
                m.reset();
        }
    }
}
