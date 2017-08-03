package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.SpriteRenderer;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ScreenAdapter;

public class GameScreen extends ScreenAdapter{

    private Core core;
    private World world;

    public GameScreen(Core core){
        this.core = core;
        WorldConfiguration config = new WorldConfigurationBuilder()
            .with(
                new SpriteRenderer()
             ).build();

        world = new World(config);

    }
}
