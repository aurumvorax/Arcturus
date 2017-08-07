package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.EntityFactory;
import aurumvorax.arcturus.artemis.GameInvocationStrategy;
import aurumvorax.arcturus.artemis.systems.MotionSystem;
import aurumvorax.arcturus.artemis.systems.SpriteRenderingSystem;
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
                new SpriteRenderingSystem(),
                new MotionSystem()
            ).register(
                new GameInvocationStrategy()
            ).build();

        world = new World(config);

        EntityFactory.init(world);
        EntityFactory.createShip(200,200,75);
    }

    @Override
    public void render(float delta){
        world.process();
    }
}
