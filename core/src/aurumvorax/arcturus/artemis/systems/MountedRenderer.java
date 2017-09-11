package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.Mounted;
import aurumvorax.arcturus.artemis.components.MountedSprite;
import aurumvorax.arcturus.artemis.components.Physics2D;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;

public class MountedRenderer extends Renderer implements RenderMarker{

    private ComponentMapper<Physics2D> mPhysics;
    private ComponentMapper<Mounted> mMounted;
    private ComponentMapper<MountedSprite> mSprite;

    private Map<String, TextureAtlas.AtlasRegion> regionsByName;
    private IntMap<TextureAtlas.AtlasRegion> regionsByID;
    private Vector2 lerpPosition = new Vector2();


    public MountedRenderer(RenderBatcher principal){
        super(principal, Aspect.all(MountedSprite.class));
    }

    @Override
    protected void initialize() {
        regionsByName = new HashMap<>();
        regionsByID = new IntMap<>();
        TextureAtlas atlas = Services.getSpriteAtlas();

        for (TextureAtlas.AtlasRegion r : atlas.getRegions())
            regionsByName.put(r.name, r);
    }

    @Override
    protected void draw(int entityID, float alpha){
        TextureRegion region = regionsByID.get(entityID);
        MountedSprite s = mSprite.get(entityID);
        Mounted m = mMounted.get(entityID);
        Physics2D parent = mPhysics.get(m.parent);
        float lerpAngle = parent.theta + (parent.omega * alpha);
        lerpPosition.set(m.location).rotate(lerpAngle).add(parent.p).mulAdd(parent.v, alpha);
        Services.batch.draw(region, lerpPosition.x - s.offsetX, lerpPosition.y - s.offsetY, s.offsetX, s.offsetY,
                region.getRegionWidth(), region.getRegionHeight(), 1, 1, m.theta + (m.omega * alpha));
    }

    @Override
    public void inserted(int entityID){
        regionsByID.put(entityID, regionsByName.get(mSprite.get(entityID).name));
        principal.register(entityID, this, mSprite.get(entityID).layer.ordinal());
    }

    @Override
    public void removed(int entityID){
        principal.unregister(entityID, this, mSprite.get(entityID).layer.ordinal());
    }
}