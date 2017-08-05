package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.IDComparator;
import aurumvorax.arcturus.IntBubbleArray;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.RenderMarker;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Sprite;
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
    private ComponentMapper<Sprite> sm;

    private Map<String, TextureAtlas.AtlasRegion> regionsByName;
    private Bag<TextureAtlas.AtlasRegion> regionsByID;
    private IntBubbleArray sortedIDs;

    public SpriteRenderer(){
        super(Aspect.all(Sprite.class, Position.class));
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
        Position p = pm.get(entityId);
        Sprite s = sm.get(entityId);

        Services.getBatch().draw(region, p.position.x - s.offsetX, p.position.y - s.offsetY);
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
