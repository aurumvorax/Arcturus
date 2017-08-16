package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.EntityFactory;
import aurumvorax.arcturus.artemis.GameInvocationStrategy;
import aurumvorax.arcturus.artemis.systems.*;
import aurumvorax.arcturus.PlayerInput;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

public class GameScreen extends ScreenAdapter{

    private Core core;
    private World world;
    private PlayerInput input;
    private PlayerControl playerControl;
    private WorldCam worldCam;
    private RenderBatcher batcher;

    public GameScreen(Core core){
        this.core = core;
        worldCam = new WorldCam();
        playerControl = new PlayerControl();
        input = new PlayerInput(core, playerControl, worldCam);
        batcher = new RenderBatcher(worldCam);

        WorldConfiguration config = new WorldConfigurationBuilder()
            .with(
                new SpriteRenderer(batcher),
                new Movement(),
                playerControl,
                worldCam
            ).register(
                new GameInvocationStrategy(batcher)
            ).build();

        world = new World(config);

        EntityFactory.init(world);
        int ship = EntityFactory.createShip(200,200,70);
        worldCam.setTarget(ship);
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void resize(int width, int height){
        worldCam.resize(width, height);
    }

    @Override
    public void render(float delta){
        world.process();
    }
}
