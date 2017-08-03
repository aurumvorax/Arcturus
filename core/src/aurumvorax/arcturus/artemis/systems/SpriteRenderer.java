package aurumvorax.arcturus.artemis.systems;

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

    private Map<String, TextureAtlas.AtlasRegion> loadRegions;
    private TextureAtlas atlas;
    private Bag<TextureAtlas.AtlasRegion> regionsByID;

    public SpriteRenderer(){
        super(Aspect.all(Sprite.class, Position.class));
    }

    @Override
    protected void initialize() {
        loadRegions = new HashMap<>();
        atlas = Services.getSpriteAtlas();
    }

    @Override
    protected void process(int entityId){

    }
}
