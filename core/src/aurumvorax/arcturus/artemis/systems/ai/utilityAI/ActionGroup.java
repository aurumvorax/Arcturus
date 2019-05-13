package aurumvorax.arcturus.artemis.systems.ai.utilityAI;

import com.badlogic.gdx.utils.IntArray;

public class ActionGroup extends Action{

    Action[] actions;
    float[] scores;
    private IntArray failedActions = new IntArray();


    ActionGroup(){}

    @Override
    protected boolean execute(float[] axisData){
        float bestScore;
        int bestAction;

        for(int k = 0; k < actions.length; k++)
            scores[k] = (actions[k].calculateScore(axisData));

        failedActions.clear();

        while(true){
            bestScore = 0;
            bestAction = -1;

            for(int k = 0; k < actions.length; k++){
                if((scores[k] > bestScore) &&(!failedActions.contains(k))){
                    bestScore = scores[k];
                    bestAction = k;
                }
            }

            if(bestAction == -1)
                return false;

            if(actions[bestAction].execute(axisData))
                return true;

            failedActions.add(bestAction);
        }
    }
}
