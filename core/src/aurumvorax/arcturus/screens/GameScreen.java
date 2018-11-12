package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.artemis.systems.PlayerShip;
import aurumvorax.arcturus.artemis.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Player;
import aurumvorax.arcturus.artemis.factories.*;
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
        PlayerInput playerInput = new PlayerInput(playerControl, worldCam);
        RenderBatcher batcher = new RenderBatcher(worldCam, hud);

        WorldConfiguration config = new WorldConfigurationBuilder()
            .register(new GameInvocationStrategy(batcher))
            .with(
                new TransitionManager(core),
                new SpriteRenderer(batcher),
                new AnimatedRenderer(batcher),
                new BeamRenderer(batcher),
                worldCam,
                hud,
                new PlayerShip(),
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
        if(core.getGameMode() == Core.GameMode.New){
            newGame();
            core.setGameMode(Core.GameMode.Active);
        }
        Gdx.input.setInputProcessor(inputMUX);
        playerControl.reset();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
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
        int ship = ShipData.buildGeneric("PlayerShip", "TestShip", "Standard", 0, 0, 0);
        ShipData.buildGeneric("Shippy McShipface", "OtherShip", "Standard", 400, -800, 0);
        int star = TerrainFactory.createStar("TestStar", 1000, 1000);
        int planet = TerrainFactory.createOrbital("TestPlanet", star);
        TerrainFactory.createOrbital("TestMoon", planet);
        ComponentMapper<Player> mPlayer = world.getMapper(Player.class);
        mPlayer.create(ship);
        PlayerShip.setTargetID(-1);
    }

    @Override
    public void dispose(){
        world.dispose();
        SaveManager.getInstance().removeAllObservers();
    }
}
