package main;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TextureAtlasPacker{

    public static void main(String args[]){


        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.edgePadding = false;
        settings.combineSubdirectories = true;
        settings.flattenPaths = true;
        TexturePacker.process(settings, "unpacked/img/sprites", "core/assets/img", "SpriteAtlas");
        TexturePacker.process(settings, "unpacked/img/animations", "core/assets/img", "AnimationAtlas");
        TexturePacker.process(settings, "unpacked/img/backgrounds", "core/assets/img", "BackgroundAtlas");

    }
}
