package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.SimpleSprite;
import aurumvorax.arcturus.artemis.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class SpriteRenderer extends Renderer implements RenderMarker{

    private ComponentMapper<Position> pm;
    private ComponentMapper<Velocity> vm;
    private ComponentMapper<SimpleSprite> sm;

    private Map<String, TextureAtlas.AtlasRegion> regionsByName;
    private Bag<TextureAtlas.AtlasRegion> regionsByID;

    public SpriteRenderer(RenderBatcher principal){
        super(principal, Aspect.all(SimpleSprite.class, Position.class, Velocity.class));
    }

    @Override
    protected void initialize() {
        regionsByName = new HashMap<>();
        TextureAtlas atlas = Services.getSpriteAtlas();
        regionsByID = new Bag<>();

        for (TextureAtlas.AtlasRegion r : atlas.getRegions())
            regionsByName.put(r.name, r);
    }

    @Override
    protected void draw(int entityId, float alpha){
        TextureRegion region = regionsByID.get(entityId);
        Position position = pm.get(entityId);
        Velocity velocity = vm.get(entityId);
        SimpleSprite s = sm.get(entityId);

        Services.batch.draw(region, position.p.x  + (velocity.v.x * alpha)- s.offsetX,
                position.p.y + (velocity.v.y * alpha) - s.offsetY, s.offsetX, s.offsetY,
                region.getRegionWidth(), region.getRegionHeight(), 1, 1, position.theta + (velocity.omega * alpha));
    }

    @Override
    public void inserted(int entityID){
        regionsByID.add(regionsByName.get(sm.get(entityID).name));
        principal.register(entityID, this, sm.get(entityID).layer.ordinal());
    }

    @Override
    public void removed(int entityID){
        principal.unregister(entityID, this, sm.get(entityID).layer.ordinal());
    }
}
