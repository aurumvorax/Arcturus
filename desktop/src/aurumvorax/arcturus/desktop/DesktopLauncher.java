package aurumvorax.arcturus.desktop;

import aurumvorax.arcturus.Core;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher {
    public static void main (String[] arg) {

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.edgePadding = false;
        settings.combineSubdirectories = true;
        TexturePacker.process(settings, "img/sprites", "img", "SpriteAtlas");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SpaceGame";
        config.useGL30 = false;
        config.width = 800;
        config.height = 600;
        config.vSyncEnabled = false;
        config.foregroundFPS = 120;
        config.backgroundFPS = 120;
        config.preferencesFileType = Files.FileType.Local;
        config.preferencesDirectory = "/saves/options";

        Gdx.app = new LwjglApplication(new Core(), config);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
