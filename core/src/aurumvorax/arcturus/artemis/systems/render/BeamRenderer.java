package aurumvorax.arcturus.artemis.systems.render;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

public class BeamRenderer extends Renderer{

    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Mounted> mMounted;
    private static ComponentMapper<Physics2D> mPhysics;

    private IntMap<TextureAtlas.AtlasRegion> regionsByID = new IntMap<>();
    private Vector2 lerpPosition = new Vector2();
    private Vector2 origin = new Vector2();


    public BeamRenderer(RenderBatcher principal){
        super(principal, Aspect.all(Beam.class, Mounted.class));
    }

    @Override
    protected void draw(int entityID, float alpha){
        Beam b = mBeam.get(entityID);
        if(b.active){
            TextureRegion region = regionsByID.get(entityID);
            Mounted m = mMounted.get(entityID);
            Physics2D parent = mPhysics.get(m.parent);

            float lerpAngle = m.theta + (m.omega * alpha);
            lerpPosition.set(m.location).rotate(parent.theta + (parent.omega * alpha)).add(parent.p).mulAdd(parent.v, alpha);
            origin.set(b.barrels.get(0)).rotate(lerpAngle).add(lerpPosition);

            Services.batch.draw(region, origin.x, origin.y - b.offsetY, 0, b.offsetY,
                    region.getRegionWidth(), region.getRegionHeight(), b.length / region.getRegionWidth(), 1, lerpAngle);
        }
    }

    @Override
    protected void inserted(int entityID){
        regionsByID.put(entityID, Services.getTexture(mBeam.get(entityID).imgName));
        principal.register(entityID, this, mBeam.get(entityID).layer.ordinal());
    }

    @Override
    protected void removed(int entityID){
        if(mBeam.has(entityID)){
            regionsByID.remove(entityID);
            principal.unregister(entityID, this, mBeam.get(entityID).layer.ordinal());
        }
    }
}
