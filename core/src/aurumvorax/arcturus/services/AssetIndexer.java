package aurumvorax.arcturus.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class AssetIndexer{

    static HashMap<String, TextureAtlas.AtlasRegion> loadImages(TextureAtlas ... atlases){
        HashMap<String, TextureAtlas.AtlasRegion> regionsByName = new HashMap<>();

        for(TextureAtlas atlas : atlases){
            for(TextureAtlas.AtlasRegion region : atlas.getRegions()){
                regionsByName.put(region.name, region);
            }
        }

        return regionsByName;
    }

    @SuppressWarnings("unchecked")
    static HashMap<String, Animation<TextureRegion>> loadAnimations(TextureAtlas animationAtlas){
        HashMap<String, Animation<TextureRegion>> animationsByName = new HashMap<>();

        FileHandle file = Gdx.files.internal("img/AnimationData.json");
        Array<AnimData> animData = Services.json.fromJson(Array.class, file);

        for(AnimData a : animData){
            animationsByName.put(a.name, new Animation(a.frameTime, animationAtlas.findRegions(a.name)));
        }

        return animationsByName;
    }


    public static class AnimData{

        private String name;
        private float frameTime;
        private int frames;
    }
}
