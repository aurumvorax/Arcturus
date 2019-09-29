package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.AIData;
import aurumvorax.arcturus.artemis.components.Player;
import aurumvorax.arcturus.artemis.components.Sensors;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class MasterAI extends IteratingSystem{

    private GunneryAI gunneryAI = new GunneryAI();

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Sensors> mSensors;


    public MasterAI(){
        super(Aspect.all(AIData.class));
    }

    @Override
    public void initialize(){
        world.inject(gunneryAI);
        //init other AI subcomponents
    }

    @Override
    protected void process(int entityId){
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
