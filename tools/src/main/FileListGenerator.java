package main;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileListGenerator{



    // Gdx.files.local gives working directory - directory jar is started from.
    // GDX.files.classpath gives files from inside jar
    // Gdx.files.internal is basically local, then if the file isn't there, it checks classpath
    // Mostly going to be using local to retrieve stuff, allows for external overrides - ie mods, later


    public static void main(String[] args){

        FileList fileList = new FileList();


        fillList(fileList.effects, Paths.get("core/assets/data/effects"));
        fillList(fileList.projectiles, Paths.get("core/assets/data/projectiles"));
        fillList(fileList.ships, Paths.get("core/assets/data/ships"));
        fillList(fileList.terrain, Paths.get("core/assets/data/terrain"));
        fillList(fileList.weapons, Paths.get("core/assets/data/weapons"));

        Path p = Paths.get("core/assets/DataFileList.json");
        Json json = new Json();
        byte[] data = json.toJson(fileList, FileList.class).getBytes();
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p))){
            out.write(data);
        }catch (IOException x){
            System.err.println(x);
        }
    }

    private static void fillList(Array<String> list, Path path){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.data")){
            for (Path entry: stream){
                System.out.println(entry);
                list.add(entry.getFileName().toString());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static class FileList{  // MUST BE IDENTICAL TO EntityData.FileList
        private Array<String> effects = new Array<>();
        private Array<String> projectiles = new Array<>();
        private Array<String> ships = new Array<>();
        private Array<String> terrain = new Array<>();
        private Array<String> weapons = new Array<>();
    }
}
