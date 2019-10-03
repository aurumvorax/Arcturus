package aurumvorax.arcturus.artemis.systems.collision;

import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.systems.Damage;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class BeamHandler implements CollisionHandler{

    private static float delta;
    private static IntBag beams = new IntBag();

    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Mounted> mMounted;

    void reset(){
        for(int i = 0; i < beams.size(); i++){
            if(mBeam.has(i)){
                Beam b = mBeam.get(beams.get(i));
                b.length = b.maxRange;
            }
        }

        beams.clear();
    }

    @Override
    public void onCollide(int actor, int beam, Collision.Manifold m){
        if(mMounted.get(beam).parent == actor)
            return;                                         // Can't shoot own ship

        Beam b = mBeam.get(beam);
        b.contactPoint.set(m.contactPoints.get(0));
        b.contact = actor;

        if(!beams.contains(beam))
            beams.add(beam);
        Damage.hull(actor, m.contactPoints.get(0), mBeam.get(beam).dps * delta);
    }

    void end(float delta){
        for(int i = 0; i < beams.size(); i++){
            Beam b = mBeam.get(beams.get(i));

            Damage.hull(b.contact, b.contactPoint, b.dps * delta);
        }
    }
}
