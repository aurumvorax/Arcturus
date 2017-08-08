package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.IDComparator;
import aurumvorax.arcturus.IntBubbleArray;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.BasicSprite;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class SpriteRenderer extends BaseEntitySystem implements RenderMarker{

    private ComponentMapper<Position> pm;
    private ComponentMapper<BasicSprite> sm;

    private Map<String, TextureAtlas.AtlasRegion> regionsByName;
    private Bag<TextureAtlas.AtlasRegion> regionsByID;
    private IntBubbleArray sortedIDs;

    public SpriteRenderer(){
        super(Aspect.all(BasicSprite.class, Position.class));
    }

    @Override
    protected void initialize() {
        regionsByName = new HashMap<>();
        TextureAtlas atlas = Services.getSpriteAtlas();
        regionsByID = new Bag<>();

        for (TextureAtlas.AtlasRegion r : atlas.getRegions())
            regionsByName.put(r.name, r);

        sortedIDs = new IntBubbleArray(new IDComparator(){
            @Override public int compare(int a, int b){ return(sm.get(a).layer.ordinal() - sm.get(b).layer.ordinal()); }
            @Override public boolean equals(int a, int b){ return(a == b); }
        });

    }

    @Override
    protected void processSystem(){
        for(int i = 0; i < sortedIDs.size(); i++)
            process(sortedIDs.get(i));
    }

    private void process(int entityId){

        TextureRegion region = regionsByID.get(entityId);
        Position position = pm.get(entityId);
        BasicSprite s = sm.get(entityId);

        Services.batch.draw(region, position.p.x - s.offsetX, position.p.y - s.offsetY, s.offsetX, s.offsetY,
                region.getRegionWidth(), region.getRegionHeight(), 1, 1, position.theta);
    }

    @Override
    public void inserted(int entityID){
        regionsByID.add(regionsByName.get(sm.get(entityID).name));
        sortedIDs.add(entityID);
    }

    @Override
    public void removed(int entityID){
        sortedIDs.remove(entityID);
    }
}
