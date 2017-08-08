package aurumvorax.arcturus.artemis;

import aurumvorax.arcturus.Services;
import com.artemis.BaseSystem;
import com.artemis.SystemInvocationStrategy;
import com.artemis.utils.BitVector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

public class GameInvocationStrategy extends SystemInvocationStrategy{

    private static final long DEFAULT_TICK_TIME = 25000000; // 25 ms = 40 TPS
    private static final long MAX_TICK_TIME = 100000000;    // 100 ms = 10 TPS
    private long currentTime;
    private long accumulator;
    private long tickTime;

    private final Array<BaseSystem> renderSystems;
    private final Array<BaseSystem> logicSystems;
    private final BitVector disabledRenderSystems = new BitVector();
    private final BitVector disabledLogicSystems = new BitVector();


    public GameInvocationStrategy(){  this(DEFAULT_TICK_TIME); }

    public GameInvocationStrategy(long tickTime){
        this.tickTime = tickTime;
        renderSystems = new Array<>();
        logicSystems = new Array<>();
    }

    @Override
    protected void initialize(){
        BaseSystem[] rawSystems = systems.getData();
        for(int i = 0; i < systems.size(); i++){
            BaseSystem rawSystem = rawSystems[i];
            if(rawSystem instanceof RenderMarker)
                renderSystems.add(rawSystem);
            else
                logicSystems.add(rawSystem);
        }
    }

    @Override
    protected void process(){
        long newTime = System.nanoTime();
        long frameTime = Math.min(newTime - currentTime, MAX_TICK_TIME);
        currentTime = newTime;
        accumulator += frameTime;

        updateEntityStates();

        while(accumulator >= tickTime){
            accumulator -= tickTime;

            // Update tick
            world.setDelta((float)tickTime * 0.000000001f);
            for(int i = 0; i < logicSystems.size; i++){
                if(!disabledLogicSystems.get(i)){
                    logicSystems.get(i).process();
                    updateEntityStates();
                }
            }
        }

        // Render call
        Services.batch.begin();
        for(int i = 0; i < renderSystems.size; i ++){
            if(!disabledRenderSystems.get(i)){

                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderSystems.get(i).process();
                updateEntityStates();
            }
        }
        Services.batch.end();
        // TODO set alpha to accumulator / 1000000000
    }

    @Override
    public boolean isEnabled(BaseSystem target){
        Array<BaseSystem> checkSystems = (target instanceof RenderMarker) ? renderSystems : logicSystems;
        BitVector checkDisabled = (target instanceof RenderMarker) ? disabledRenderSystems : disabledLogicSystems;
        Class targetClass = target.getClass();
        for(int i = 0; i < checkSystems.size; i++){
            if(targetClass == checkSystems.get(i).getClass())
                return !checkDisabled.get(i);
        }
        throw new RuntimeException("System not found - " + target);
    }

    @Override
    public void setEnabled(BaseSystem target, boolean value){
        Array<BaseSystem> checkSystems = (target instanceof RenderMarker) ? renderSystems : logicSystems;
        BitVector checkDisabled = (target instanceof RenderMarker) ? disabledRenderSystems : disabledLogicSystems;
        Class targetClass = target.getClass();
        for(int i = 0; i < checkSystems.size; i++){
            if(targetClass == checkSystems.get(i).getClass()){
                checkDisabled.set(i, value);
                return;
            }
        }
        throw new RuntimeException("System not found - " + target);
    }
}
