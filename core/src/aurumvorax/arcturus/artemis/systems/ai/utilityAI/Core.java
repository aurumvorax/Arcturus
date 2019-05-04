package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;

public class Core{

    protected Array<Input> inputs;
    private IntArray inputData;
    private IntMap<String> inputNames;

    protected Array<Action> actions;
    protected IntArray axisData;



    protected Core(){
        inputs = new Array<>();
        inputData = new IntArray();
        inputNames = new IntMap<>();

        actions = new Array<>();
        axisData = new IntArray();
    }
}
