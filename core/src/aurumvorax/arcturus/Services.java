package aurumvorax.arcturus;

import aurumvorax.arcturus.artemis.IntMapSerializer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;

public enum Services{
    INSTANCE;

    public static final String SPRITE_ATLAS_PATH = "img/SpriteAtlas.atlas";
    public static final String SHIP_IMG_PATH = "ships/";
    public static final String PROJECTILE_IMG_PATH = "projectiles/";
    public static final String WEAPON_IMG_PATH = "weapons/";
    public static final String KEY_PATH = "config/keys.cfg";

    public static final FileHandle SHIP_PATH = Gdx.files.local("data/ships/");
    public static final FileHandle PROJECTILE_PATH = Gdx.files.local("data/projectiles");
    public static final FileHandle WEAPON_PATH = Gdx.files.local("data/weapons");

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
    }

    public static boolean loadAssets(){ return assetManager.update(); }
    public static float loadProgress(){ return assetManager.getProgress(); }
    public static TextureAtlas getSpriteAtlas(){ return assetManager.get(SPRITE_ATLAS_PATH); }
}
