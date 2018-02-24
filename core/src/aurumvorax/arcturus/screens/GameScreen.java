package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.*;
import aurumvorax.arcturus.artemis.components.shipComponents.PlayerShip;
import aurumvorax.arcturus.artemis.systems.*;
import aurumvorax.arcturus.PlayerInput;
import aurumvorax.arcturus.artemis.systems.ai.behaviour.ShipAI;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import aurumvorax.arcturus.artemis.systems.render.*;
import aurumvorax.arcturus.savegame.SaveManager;
import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;

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
        playerControl = new PlayerControl();
        hud = new HUDRenderer();
        PlayerInput playerInput = new PlayerInput(core, playerControl, worldCam);
        RenderBatcher batcher = new RenderBatcher(worldCam, hud);

        WorldConfiguration config = new WorldConfigurationBuilder()
            .register(new GameInvocationStrategy(batcher))
            .with(
                new SpriteRenderer(batcher),
                new AnimatedRenderer(batcher),
                new BeamRenderer(batcher),
                worldCam,
                hud,
                playerControl,
                new Proximity(),
                new ShipAI(),
                new Movement(),
                new Orbital(),
                new WeaponsUpdate(),
                new Collision(),
                new EphemeralDecay(),
                new Damage(),
                new Destructor()
            ).build();
        world = new World(config);

        ShipFactory.init(world);
        WeaponFactory.init(world);
        ProjectileFactory.init(world);
        EffectFactory.init(world);
        TerrainFactory.init(world);

        inputMUX = new InputMultiplexer();
        inputMUX.addProcessor(hud.getInputProcessor());
        inputMUX.addProcessor(playerInput);

        worldSerializer = new WorldSerializer(world);
        SaveManager.getInstance().addObserver(worldSerializer);
        SaveManager.getInstance().addObserver(worldCam);
    }

    @Override
    public void show(){
        if(!Core.getActive()){
            newGame();
            Core.setActive(true);
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
        worldCam.reset();
        worldSerializer.resetWorld();
        int ship = ShipFactory.create("TestShip", "Standard", 0, 0, 0);
        //ShipFactory.create("TestShip", "Standard", 200,200,0);
        ShipFactory.create("OtherShip", "Standard", 400, -800, 0);
        int star = TerrainFactory.createStar("TestStar", 1000, 1000);
        int planet = TerrainFactory.createOrbital("TestPlanet", star);
        TerrainFactory.createOrbital("TestMoon", planet);
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
