package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Inertia;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Velocity;
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

    private ComponentMapper<Position> mPosition;
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Inertia> mInertia;

    @Override
    public void onCollide(int entityA, int entityB, Collision.Manifold m){

        // Newtonian objects bounce, then get mass proportional position correction
        if(mInertia.has(entityA) && mInertia.has(entityB)){
            bounce(entityA, entityB, m);
            float correction = m.penetration[0] / (mInertia.get(entityA).invMass + mInertia.get(entityB).invMass) * 0.5f;
            mPosition.get(entityA).p.mulAdd(m.normal, -mInertia.get(entityA).invMass * correction);
            mPosition.get(entityB).p.mulAdd(m.normal, mInertia.get(entityB).invMass * correction);

        }else{      // Non Newtonian objects just get simple position correction
            Vector2 correction = new Vector2(m.normal).scl(m.penetration[0] * 0.5f);
            mPosition.get(entityA).p.sub(correction);
            mPosition.get(entityB).p.add(correction);
        }
    }


    private void bounce(int entityA, int entityB, Collision.Manifold m){
        Velocity velA = mVelocity.get(entityA);
        Velocity velB = mVelocity.get(entityB);
        Inertia inertA = mInertia.get(entityA);
        Inertia inertB = mInertia.get(entityB);

        for(int i = 0; i < m.contacts; i++){
            Vector2 rs1 = new Vector2(m.contactPoints.get(i)).sub(mPosition.get(entityA).p);
            Vector2 rs2 = new Vector2(m.contactPoints.get(i)).sub(mPosition.get(entityB).p);
            rv.set(velB.v).add(cross(rs2, velB.omega * RADS)).sub(velA.v).sub(cross(rs1, velA.omega * RADS));
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

            rv.set(velB.v).add(cross(rs2, velB.omega * RADS)).sub(velA.v).sub(cross(rs1, velA.omega * RADS));
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
        Velocity velocity = mVelocity.get(entity);
        Inertia inertia = mInertia.get(entity);
        velocity.v.mulAdd(i, inertia.invMass);
        velocity.omega -= Math.toDegrees(i.crs(contactVector) * inertia.invInertia);
    }

    private static Vector2 cross(Vector2 v, float a){
        return new Vector2(v.y * -a, v.x * a);
    }
}
