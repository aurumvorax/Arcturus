package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Sensors;
import aurumvorax.arcturus.artemis.components.Ship;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class MasterAI extends IteratingSystem{

    private SensorsAI sensorsAI = new SensorsAI();
    private GunneryAI gunneryAI = new GunneryAI();
    private TargetingAI targetingAI = new TargetingAI();

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Sensors> mSensors;


    public MasterAI(){
        super(Aspect.all(Ship.class).exclude(Player.class));
    }

    @Override
    public void initialize(){
        world.inject(sensorsAI);
        world.inject(gunneryAI);
        world.inject(targetingAI);

        //init other AI subcomponents
    }

    @Override
    protected void process(int entityId){

        sensorsAI.process(entityId);
        targetingAI.process(entityId);
        if(mPlayer.has(entityId)){
            //autopilot?
            //player weapons control - handled through PlayerControl
            //auto weapons control
        }else{
            //main and auto gunnery control
            //pilot
        }

        if((mSensors.has(entityId) && (mSensors.get(entityId).scanForMissiles))){
            //Gunnery PD
        }
    }
}
