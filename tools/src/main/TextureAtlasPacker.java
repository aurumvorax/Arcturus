package main;

import aurumvorax.arcturus.services.AssetIndexer.AnimData;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextureAtlasPacker{

    public static void main(String args[]){

        Array<AnimData> animationList = new Array<>();
        Json json = new Json();

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.edgePadding = false;
        settings.combineSubdirectories = true;
        settings.flattenPaths = true;

        TexturePacker.process(settings, "unpacked/img/sprites", "core/assets/img", "SpriteAtlas");
        TexturePacker.process(settings, "unpacked/img/animations", "core/assets/img", "AnimationAtlas");
        TexturePacker.process(settings, "unpacked/img/backgrounds", "core/assets/img", "BackgroundAtlas");
        TexturePacker.process(settings, "unpacked/img/ui", "core/assets/img", "UIAtlas");

        Path path = Paths.get("unpacked/img/animations");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.data")){
            for(Path entry: stream){
                try(FileInputStream fis = new FileInputStream(entry.toFile())){
                    animationList.add(json.fromJson(AnimData.class, fis));
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        path = Paths.get("core/assets/img/AnimationData.json");
        byte[] data = json.toJson(animationList, Array.class).getBytes();
        try(OutputStream out = new BufferedOutputStream(Files.newOutputStream(path))){
            out.write(data);
        }catch(IOException x){
            x.printStackTrace();
        }


    }
}
