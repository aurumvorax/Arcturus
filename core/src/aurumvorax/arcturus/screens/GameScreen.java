package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.GameInvocationStrategy;
import aurumvorax.arcturus.artemis.ProjectileFactory;
import aurumvorax.arcturus.artemis.ShipFactory;
import aurumvorax.arcturus.artemis.WeaponFactory;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.systems.*;
import aurumvorax.arcturus.PlayerInput;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import com.artemis.*;
import com.artemis.utils.IntBag;
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
                new MountedRenderer(batcher),
                new Movement(),
                new Collision(),
                new WeaponsUpdate(),
                new EphemeralDecay(),
                playerControl,
                worldCam
            ).register(
                new GameInvocationStrategy(batcher)
            ).build();

        world = new World(config);
        ShipFactory.init(world);
        WeaponFactory.init(world);
        ProjectileFactory.init(world);
    }

    @Override
    public void show(){
        switch(core.getMode()){
            case NEW:
                newGame();
                core.setMode(Core.GameMode.ACTIVE);
            break;

            case LOAD:
                // TODO populate world from active save file
                core.setMode(Core.GameMode.ACTIVE);
            break;

            case ACTIVE:
            break;          // world already populated, just dive right in
        }
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

    private void newGame(){
        resetWorld();
        int ship = ShipFactory.create("TestShip", "Standard", 200, 200, 0);
        ShipFactory.create("TestShip", "Standard", 0,0,45);
        ShipFactory.create("OtherShip", "Standard", 400, 400, 135);
        worldCam.setTarget(ship);
        ComponentMapper<PlayerShip> mPlayer = world.getMapper(PlayerShip.class);
        mPlayer.create(ship);
    }

    private void resetWorld(){
        IntBag entities = world.getAspectSubscriptionManager()
                .get(Aspect.all())
                .getEntities();

        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            world.delete(ids[i]);
        }
    }
}
