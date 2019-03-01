package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.factories.EffectData;
import aurumvorax.arcturus.services.EntityData;
import aurumvorax.arcturus.services.Services;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.EntityId;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Trails extends IteratingSystem{

    private static Bag<TrailData> fadingTrails = new Bag<>();

    private static ComponentMapper<aurumvorax.arcturus.artemis.components.Trail> mTrail;
    private static ComponentMapper<Physics2D> mPhysics;


    public Trails(){
        super(Aspect.all(Physics2D.class, aurumvorax.arcturus.artemis.components.Trail.class));
    }

    public static Bag<TrailData> getFadingTrails(){ return fadingTrails; }

    @Override
    protected void process(int entityId){
        mTrail.get(entityId).trailData.update(true);
    }

    @Override
    protected void removed(int entityID){
        fadingTrails.add(mTrail.get(entityID).trailData);
    }

    @Override
    protected void end(){
        for(TrailData t : fadingTrails)
            t.update(false);
    }

    public static TrailData addTrail(int entityID, String name, Vector2 offset){
        TrailData t = new TrailData();
        EffectData data = EntityData.getEffectData(name);
        t.parent = entityID;
        t.offset.set(offset);
        t.texture = Services.getTexture(data.textureName);
        t.length = data.segments;
        t.texDiv = (t.texture.getU2() - t.texture.getU()) / t.length;
        t.segments = new TrailData.Segment[data.segments];
        t.ticksPerSegment = data.interval;
        t.width = data.width;
        t.widen = data.widen;

        Physics2D p = mPhysics.get(entityID);
        t.point.set(p.p).add(t.offset);
        t.create(t.point, p.v.angle(), t.width, t.widen);

        return t;
    }

    public static class TrailData{

        public TextureAtlas.AtlasRegion texture;
        public Segment[] segments;
        public int size = 0;
        public int length;
        public int index = -1;
        public float texDiv;

        @EntityId int parent;
        private int ticks;
        private int ticksPerSegment;
        private float width;
        private float widen;
        private Vector2 point = new Vector2();
        private Vector2 lastPoint = new Vector2();
        private Vector2 offset = new Vector2();


        void update(boolean active){
            ticks++;
            if(ticks >= ticksPerSegment){
                ticks = 0;

                if(active){
                    point.set(mPhysics.get(parent).p).add(offset);
                    lastPoint.sub(point);
                    create(point, lastPoint.angle(), width, widen);
                    lastPoint.set(point);
                }else{
                    size--;
                    if(size < 2)
                        fadingTrails.remove(this);
                }

                for(int i = 0; i < size; i++)
                    segments[i].update();
            }
        }

        void create(Vector2 center, float angle, float width, float widen){
            Segment newSeg = new Segment();
            newSeg.left.set(width / 2f, 0).setAngle(Utils.normalize(angle + 90)).add(center);
            newSeg.right.set(width / 2f, 0).setAngle(Utils.normalize(angle - 90)).add(center);
            newSeg.offsetV.set(widen, 0).setAngle(Utils.normalize(angle - 90));

            segments[++index] = newSeg;
            if(index == length - 1)
                index = -1;

            if(size != length)
                size++;
        }

        public static class Segment{
            public Vector2 left = new Vector2();
            public Vector2 right = new Vector2();
            Vector2 offsetV = new Vector2();

            void update(){
                right.add(offsetV);
                left.sub(offsetV);
            }
        }
    }


}
