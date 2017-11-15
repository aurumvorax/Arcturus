package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.systems.Damage;
import com.artemis.ComponentMapper;

public class BeamHandler implements CollisionHandler{

    private static float delta;

    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Mounted> mMounted;

    public void setDelta(float d){ delta = d; }

    @Override
    public void onCollide(int actor, int beam, Collision.Manifold m){
        if(mMounted.get(beam).parent == actor)
            return;                                         // Can't shoot own ship

        Damage.hull(actor, m.contactPoints.get(0), mBeam.get(beam).dps * delta);
    }
}
