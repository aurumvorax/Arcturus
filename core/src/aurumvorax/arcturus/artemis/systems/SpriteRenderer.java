package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.MountedSprite;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.SimpleSprite;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;


public class SpriteRenderer extends Renderer implements RenderMarker{

    private ComponentMapper<Physics2D> mPhysics;
    private ComponentMapper<SimpleSprite> mSprite;

    private Map<String, TextureAtlas.AtlasRegion> regionsByName;
    private IntMap<TextureAtlas.AtlasRegion> regionsByID;

    public SpriteRenderer(RenderBatcher principal){
        super(principal, Aspect.all(SimpleSprite.class, Physics2D.class));
    }

    @Override
    protected void initialize() {
        regionsByName = new HashMap<>();
        TextureAtlas atlas = Services.getSpriteAtlas();
        regionsByID = new IntMap<>();

        for (TextureAtlas.AtlasRegion r : atlas.getRegions())
            regionsByName.put(r.name, r);
    }

    @Override
    protected void draw(int entityID, float alpha){
        TextureRegion region = regionsByID.get(entityID);
        Physics2D physics2D = mPhysics.get(entityID);
        SimpleSprite s = mSprite.get(entityID);

        Services.batch.draw(region, physics2D.p.x  + (physics2D.v.x * alpha)- s.offsetX,
                physics2D.p.y + (physics2D.v.y * alpha) - s.offsetY, s.offsetX, s.offsetY,
                region.getRegionWidth(), region.getRegionHeight(), 1, 1, physics2D.theta + (physics2D.omega * alpha));
    }

    @Override
    public void inserted(int entityID){
        regionsByID.put(entityID, regionsByName.get(mSprite.get(entityID).name));
        principal.register(entityID, this, mSprite.get(entityID).layer.ordinal());
    }

    @Override
    public void removed(int entityID){
        if(mSprite.has(entityID)){
            regionsByID.remove(entityID);
            principal.unregister(entityID, this, mSprite.get(entityID).layer.ordinal());
        }
    }
}
