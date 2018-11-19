package aurumvorax.tools;

import aurumvorax.arcturus.Services;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

public class FileListGenerator{



    // Gdx.files.local gives working directory - directory jar is started from.
    // GDX.files.classpath gives files from inside jar
    // Gdx.files.internal is basically local, then if the file isn't there, it checks classpath


    public static void main(String[] args){

        FileList fileList = new FileList();

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

        fillList(fileList.effects, FileSystems.getDefault().getPath(Services.EFFECT_PATH));
        fillList(fileList.projectiles, FileSystems.getDefault().getPath(Services.PROJECTILE_PATH));
        fillList(fileList.ships, FileSystems.getDefault().getPath(Services.SHIP_PATH));
        fillList(fileList.terrain, FileSystems.getDefault().getPath(Services.TERRAIN_PATH));
        fillList(fileList.weapons, FileSystems.getDefault().getPath(Services.WEAPON_PATH));

        Path p = Paths.get("DataFileList.json");
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
