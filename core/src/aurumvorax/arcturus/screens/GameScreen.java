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
import aurumvorax.arcturus.savegame.SaveManager;
import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter{

    private Core core;
    private World world;
    private PlayerControl playerControl;
    private WorldCam worldCam;
    private HUDRenderer hud;
    private WorldSerializer worldSerializer;
    private InputMultiplexer inputMUX;

    public GameScreen(Core core){

        this.core = core;
        worldCam = new WorldCam();
        inputMUX = new InputMultiplexer();
        playerControl = new PlayerControl();
        hud = new HUDRenderer();
        PlayerInput playerInput = new PlayerInput(core, playerControl, worldCam);
        RenderBatcher batcher = new RenderBatcher(worldCam, hud);

        WorldConfiguration config = new WorldConfigurationBuilder()
            .register(new GameInvocationStrategy(batcher))
            .with(
                new SpriteRenderer(batcher),
                new MountedRenderer(batcher),
                new BeamRenderer(batcher),
                worldCam,
                hud,
                playerControl,
                new Movement(),
                new WeaponsUpdate(),
                new Collision(),
                new EphemeralDecay()
            ).build();
        world = new World(config);

        ShipFactory.init(world);
        WeaponFactory.init(world);
        ProjectileFactory.init(world);

        inputMUX = new InputMultiplexer();
        inputMUX.addProcessor(hud.getInputProcessor());
        inputMUX.addProcessor(playerInput);

        worldSerializer = new WorldSerializer(world);
        SaveManager.getInstance().addObserver(worldSerializer);
        SaveManager.getInstance().addObserver(worldCam);
    }

    @Override
    public void show(){
        if(!core.getActive()){
            newGame();
            core.setActive(true);
        }
        Gdx.input.setInputProcessor(inputMUX);
        playerControl.reset();
    }

    @Override
    public void resize(int width, int height){
        worldCam.resize(width, height);
        hud.resize(width,height);
    }

    @Override
    public void render(float delta){
        world.process();
    }

    private void newGame(){
        worldSerializer.resetWorld();
        int ship = ShipFactory.create("TestShip", "Standard", 0, 0, 0);
        ShipFactory.create("TestShip", "Standard", 200,0,0);
        ShipFactory.create("OtherShip", "Standard", 400, -200, 0);
        worldCam.setTarget(ship);
        ComponentMapper<PlayerShip> mPlayer = world.getMapper(PlayerShip.class);
        mPlayer.create(ship);
    }

    @Override
    public void dispose(){
        world.dispose();
        SaveManager.getInstance().removeAllObservers();
    }
}
