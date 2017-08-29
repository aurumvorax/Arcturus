package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Inertia;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public enum BounceHandler implements CollisionHandler{
    INSTANCE;

    private final static float RADS =  0.01745f;
    private final static float e = 0.6f;
    private final static float cofs = 0.4f;
    private final static float cofd = 0.2f;
    private static Vector2 impulseVector = new Vector2();
    private static Vector2 rv = new Vector2();
    private static Vector2 tangent = new Vector2();

    private ComponentMapper<Physics2D> mPhysics2D;
    private ComponentMapper<Inertia> mInertia;

    @Override
    public void onCollide(int entityA, int entityB, Collision.Manifold m){

        // Newtonian objects bounce, then get mass proportional position correction
        if(mInertia.has(entityA) && mInertia.has(entityB)){
            bounce(entityA, entityB, m);
            float correction = m.penetration[0] / (mInertia.get(entityA).invMass + mInertia.get(entityB).invMass) * 0.5f;
            mPhysics2D.get(entityA).p.mulAdd(m.normal, -mInertia.get(entityA).invMass * correction);
            mPhysics2D.get(entityB).p.mulAdd(m.normal, mInertia.get(entityB).invMass * correction);

        }else{      // Non Newtonian objects just get simple position correction
            Vector2 correction = new Vector2(m.normal).scl(m.penetration[0] * 0.5f);
            mPhysics2D.get(entityA).p.sub(correction);
            mPhysics2D.get(entityB).p.add(correction);
        }
    }


    private void bounce(int entityA, int entityB, Collision.Manifold m){
        Physics2D physicsA = mPhysics2D.get(entityA);
        Physics2D physicsB = mPhysics2D.get(entityB);
        Inertia inertA = mInertia.get(entityA);
        Inertia inertB = mInertia.get(entityB);

        for(int i = 0; i < m.contacts; i++){
            Vector2 rs1 = new Vector2(m.contactPoints.get(i)).sub(mPhysics2D.get(entityA).p);
            Vector2 rs2 = new Vector2(m.contactPoints.get(i)).sub(mPhysics2D.get(entityB).p);
            rv.set(physicsB.v).add(cross(rs2, physicsB.omega * RADS)).sub(physicsA.v).sub(cross(rs1, physicsA.omega * RADS));
            float rs = rv.dot(m.normal);
            if(rs > 0)
                return;

            float s1irad = rs1.crs(m.normal);
            float s2irad = rs2.crs(m.normal);
            float invMoment = inertA.invMass + inertB.invMass + (inertA.invInertia * s1irad * s1irad)
                    + (inertB.invInertia * s2irad * s2irad);
            float j = -(1.0f + e) * rs;
            j /= invMoment * m.contacts;

            impulseVector.set(m.normal).scl(j);
            impulse(entityB, impulseVector, rs2);
            impulse(entityA, impulseVector.scl(-1.0f),  rs1);

            rv.set(physicsB.v).add(cross(rs2, physicsB.omega * RADS)).sub(physicsA.v).sub(cross(rs1, physicsA.omega * RADS));
            tangent.set(rv).mulAdd(m.normal, rv.dot(m.normal)).nor();
            float jt = rv.dot(tangent);
            jt /= invMoment * m.contacts;
            if(Math.abs(jt) < j * cofs)
                impulseVector.set(tangent).scl(jt);
            else
                impulseVector.set(tangent).scl(-j * cofd);

            impulse(entityB, impulseVector, rs2);
            impulse(entityA, impulseVector.scl(-1.0f),  rs1);
        }
    }

    private void impulse(int entity, Vector2 i, Vector2 contactVector){
        Physics2D physics = mPhysics2D.get(entity);
        Inertia inertia = mInertia.get(entity);
        physics.v.mulAdd(i, inertia.invMass);
        physics.omega -= Math.toDegrees(i.crs(contactVector) * inertia.invInertia);
    }

    private static Vector2 cross(Vector2 v, float a){
        return new Vector2(v.y * -a, v.x * a);
    }
}
