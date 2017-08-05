package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.Services;
import aurumvorax.arcturus.artemis.GameInvocationStrategy;
import aurumvorax.arcturus.artemis.components.Position;
import aurumvorax.arcturus.artemis.components.Sprite;
import aurumvorax.arcturus.artemis.systems.SpriteRenderer;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter{

    private Core core;
    private World world;

    public GameScreen(Core core){
        this.core = core;
        WorldConfiguration config = new WorldConfigurationBuilder()
            .with(
                new SpriteRenderer()
            ).register(
                new GameInvocationStrategy()
            ).build();

        world = new World(config);

        int ship = world.create();
        world.getMapper(Position.class).create(ship);
        world.getMapper((Position.class)).get(ship).position = new Vector2();
        world.getMapper(Sprite.class).create(ship).name = Services.SHIP_IMG_PATH + "TestShip";
    }

    @Override
    public void render(float delta){
        world.process();
    }
}
