package aurumvorax.arcturus;

import aurumvorax.arcturus.options.IntMapSerializer;
import aurumvorax.arcturus.options.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

public enum Services{
    INSTANCE;

    private static Map<String, TextureAtlas.AtlasRegion> regionsByName = new HashMap<>();
    private static Map<String, Animation<TextureRegion>> animationsByName = new HashMap<>();

    private static final String MENU_SKIN_PATH = "skin/neon-ui.json";
    private static final String SPRITE_ATLAS_PATH = "img/SpriteAtlas.atlas";
    private static final String ANIMATION_ATLAS_PATH = "img/AnimationAtlas.atlas";

    public static final String KEY_PATH = "config/keys.cfg";
    public static final String SAVE_PATH = "saves/";

    public static final FileHandle SHIP_PATH = Gdx.files.local("data/ships/");
    public static final FileHandle PROJECTILE_PATH = Gdx.files.local("data/projectiles");
    public static final FileHandle WEAPON_PATH = Gdx.files.local("data/weapons");
    public static final Skin MENUSKIN = new Skin(Gdx.files.internal(MENU_SKIN_PATH));

    private static final AssetManager assetManager = new AssetManager();

    public static final SpriteBatch batch = new SpriteBatch();
    public static final Json json = new Json();
    public static final Keys keys = new Keys();
    
    public static void initJson(){
        Services.json.addClassTag("Command", Keys.Command.class);
        Services.json.addClassTag("Vector2", Vector2.class);
        Services.json.setSerializer(IntMap.class, new IntMapSerializer());
    }

    public static void queueTextureAssets(){
        assetManager.load(SPRITE_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(ANIMATION_ATLAS_PATH, TextureAtlas.class);
    }

    public static boolean loadAssets(){ return assetManager.update(); }
    public static float loadProgress(){ return assetManager.getProgress(); }
    public static TextureAtlas getSpriteAtlas(){ return assetManager.get(SPRITE_ATLAS_PATH); }

    public static void initAssets(){
        for(TextureAtlas.AtlasRegion region : getSpriteAtlas().getRegions())
            regionsByName.put(region.name, region);
        loadAnimations();
    }

    public static TextureAtlas.AtlasRegion getTexture(String name){
        return regionsByName.get(name);
    }

    public static Animation getAnimation(String name){
        return animationsByName.get(name);
    }

    private static void loadAnimations(){
        TextureAtlas atlas = assetManager.get(ANIMATION_ATLAS_PATH);
        animationsByName.put("Boom", new Animation<TextureRegion>(0.01f, atlas.findRegions("explosion 3")));
    }
}
