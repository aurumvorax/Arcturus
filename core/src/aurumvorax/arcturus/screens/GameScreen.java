package aurumvorax.arcturus.screens;

import aurumvorax.arcturus.Core;
import aurumvorax.arcturus.PlayerInput;
import aurumvorax.arcturus.artemis.GameInvocationStrategy;
import aurumvorax.arcturus.artemis.WorldSerializer;
import aurumvorax.arcturus.artemis.components.Faction;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Weapons;
import aurumvorax.arcturus.artemis.factories.*;
import aurumvorax.arcturus.artemis.systems.*;
import aurumvorax.arcturus.artemis.systems.ai.MasterAI;
import aurumvorax.arcturus.artemis.systems.ai.missile.MissileAI;
import aurumvorax.arcturus.artemis.systems.collision.Collision;
import aurumvorax.arcturus.artemis.systems.collision.Selection;
import aurumvorax.arcturus.artemis.systems.render.*;
import aurumvorax.arcturus.galaxy.SolarSystemManager;
import aurumvorax.arcturus.savegame.SaveManager;
import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;

public class GameScreen extends ScreenAdapter{

    private Core core;
    private PlayerControl playerControl;
    private WorldCam worldCam;
    private HUDRenderer hud;
    private WorldSerializer worldSerializer;
    private InputMultiplexer inputMUX;
    private static World world;
    private static SolarSystemManager stellarManager;

    public GameScreen(Core core){

        this.core = core;
        worldCam = new WorldCam();
        playerControl = new PlayerControl();
        hud = new HUDRenderer();
        stellarManager = new SolarSystemManager();
        PlayerInput playerInput = new PlayerInput(core, playerControl, worldCam, hud);
        RenderBatcher batcher = new RenderBatcher(worldCam, hud);

        WorldConfiguration config = new WorldConfigurationBuilder()
            .register(new GameInvocationStrategy(core, batcher))
            .with(
                new TransitionManager(core),
                new SpriteRenderer(batcher),
                new AnimatedRenderer(batcher),
                new BeamRenderer(batcher),
                new TrailRenderer(batcher),
                worldCam,
                hud,
                new PlayerShip(),
                playerControl,
                new Proximity(),
                new MasterAI(),
                new Movement(),
                new Orbital(),
                new WeaponsUpdate(),
                new MissileAI(),
                new Collision(),
                new Selection(),
                new EphemeralDecay(),
                new Trails(),
                new Damage(),
                new Destructor()
            ).build();

        world = new World(config);

        ShipFactory.init(world);
        WeaponFactory.init(world);
        ProjectileFactory.init(world);
        EffectFactory.init(world);
        StellarFactory.init(world);


        inputMUX = new InputMultiplexer();
        inputMUX.addProcessor(hud.getInputProcessor());
        inputMUX.addProcessor(playerInput);

        worldSerializer = new WorldSerializer(world);

        SaveManager saveManager = SaveManager.INSTANCE;
        saveManager.addObserver(worldSerializer);
        saveManager.addObserver(worldCam);
        saveManager.addObserver(stellarManager);
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
        resetWorld();
        int ship = ShipFactory.create("PlayerShip", "TestShip", "Standard", 800, 800, 0);
        ComponentMapper<Player> mPlayer = world.getMapper(Player.class);
        mPlayer.create(ship);
        world.getMapper(Faction.class).get(ship).faction = "Player";
        world.getMapper(Weapons.class).get(ship).pd.clear();

        int shippy = ShipFactory.create("Shippy McShipface", "OtherShip", "Standard", 400, -800, 0);
        int bob = ShipFactory.create("Bob", "OtherShip", "Standard", 650, -800, 165);
        world.getMapper(Weapons.class).get(shippy).main.clear();
        world.getMapper(Weapons.class).get(bob).main.clear();

        PlayerShip.setTargetID(-1);
        SolarSystemManager.resetWorlds();
        SolarSystemManager.loadSystem("Playground");
    }

    public static void resetWorld(){
        IntBag entities = world.getAspectSubscriptionManager()
                .get(Aspect.all())
                .getEntities();

        int[] ids = entities.getData();
        for (int i = 0, s = entities.size(); s > i; i++) {
            world.delete(ids[i]);
        }
        world.process();
        world.getEntityManager().reset();
    }

    @Override
    public void dispose(){
        world.dispose();
        SaveManager.INSTANCE.removeAllObservers();
    }
}
