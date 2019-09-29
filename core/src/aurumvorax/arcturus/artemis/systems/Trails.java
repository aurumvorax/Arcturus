package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Trail;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class Trails extends IteratingSystem{

    private static ComponentMapper<aurumvorax.arcturus.artemis.components.Trail> mTrail;
    private static ComponentMapper<Physics2D> mPhysics;


    public Trails(){
        super(Aspect.all(aurumvorax.arcturus.artemis.components.Trail.class));
    }

    @Override
    protected void process(int entityId){
        Trail t = mTrail.get(entityId);

        if(!mPhysics.has(t.parent))
            t.active = false;

        if(t.active){
            t.offset.setAngle(180 + mPhysics.get(t.parent).theta);
            t.point.set(mPhysics.get(t.parent).p).add(t.offset);
            t.lastPoint.sub(t.point);
            createSegment(t, t.point, t.lastPoint.angle());
            t.lastPoint.set(t.point);

        }else{
            t.size--;
            if(t.size < 2)
                Destructor.safeRemove(entityId);
        }

        for(int i = 0; i < t.size; i++)
            t.segments[i].update();
    }

    @Override
    protected void inserted(int entityID){
        Trail t = mTrail.get(entityID);
        Physics2D p = mPhysics.get(t.parent);

        t.point.set(p.p).add(t.offset);
        createSegment(t, t.point, p.v.angle());
    }

    private void createSegment(Trail t, Vector2 center, float angle){
            Trail.Segment newSeg = new Trail.Segment();
            newSeg.left.set(t.width / 2f, 0).setAngle(Utils.normalize(angle + 90)).add(center);
            newSeg.right.set(t.width / 2f, 0).setAngle(Utils.normalize(angle - 90)).add(center);
            newSeg.offsetV.set(t.widen, 0).setAngle(Utils.normalize(angle - 90));

            t.segments[++t.index] = newSeg;
            if(t.index == t.length - 1)
                t.index = -1;

            if(t.size != t.length)
                t.size++;

    }


}
