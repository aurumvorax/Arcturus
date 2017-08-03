package aurumvorax.arcturus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum Services{
    INSTANCE;

    public static final String SPRITE_ATLAS_PATH = "img/SpriteAtlas.atlas";

    public static final String SHIP_IMG_PATH = "ships/";
    public static final String PROJECTILE_IMG_PATH = "projectiles/";
    public static final String WEAPON_IMG_PATH = "weapons/";

    private static final AssetManager assetManager = new AssetManager();
    
    
    public static void queueTextureAssets(){
        assetManager.load(SPRITE_ATLAS_PATH, TextureAtlas.class);
    }

    public static boolean loadAssets(){ return assetManager.update(); }
    public static float loadProgress(){ return assetManager.getProgress(); }

    public static TextureAtlas getSpriteAtlas(){ return assetManager.get(SPRITE_ATLAS_PATH); }
}
