package aurumvorax.arcturus.artemis.systems;

import aurumvorax.arcturus.IDComparator;
import aurumvorax.arcturus.IntBubbleArray;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Sprite;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class SpriteRenderer extends IteratingSystem{

    ComponentMapper<Position> pm;
    ComponentMapper<Sprite> sm;

    private Map<String, TextureAtlas.AtlasRegion> regions;
    private TextureAtlas atlas;
    private Bag<TextureAtlas.AtlasRegion> regionsByID;
    private IntBubbleArray sortedIDs;

    public SpriteRenderer(){
        super(Aspect.all(Sprite.class, Position.class));
    }

    @Override
    protected void initialize() {
        regions = new HashMap<>();
        atlas = Services.getSpriteAtlas();
        regionsByID = new Bag<>();
        for (TextureAtlas.AtlasRegion r : atlas.getRegions())
            regions.put(r.name, r);
        sortedIDs = new IntBubbleArray(new IDComparator(){
            @Override public int compare(int a, int b){ return(sm.get(a).layer.ordinal() - sm.get(b).layer.ordinal()); }
            @Override public boolean equals(int a, int b){ return(a == b); }
        });

    }

    @Override
    protected void process(int entityId){

    }
}
