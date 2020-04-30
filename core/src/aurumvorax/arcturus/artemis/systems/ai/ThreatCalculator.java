package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class ThreatCalculator implements Scheduled{

    private static ComponentMapper<AIData> mData;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Health> mHealth;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Launcher> mLauncher;


    @Override
    public void runTask(int entityId){
        AIData data = mData.get(entityId);

        if(data.dirty){
            data.threatOffence = calcOffence(entityId);
            data.threatDefence = calcDefence(entityId);

            data.dirty = false;
        }
    }

    private float calcOffence(int ship){
        float dps = 0;
        IntBag w = mWeapons.get(ship).main;

        for(int i = 0; i < w.size(); i++){
        }

        return dps;
    }

    private float calcDefence(int ship){
        return mHealth.get(ship).hull;

        // will add shields and PD weapons here eventually
    }

    private float threatCalc(int ship){
        if(!mData.has(ship))
            return 0;

        AIData data = mData.get(ship);
        return data.threatDefence + data.threatOffence;

        // later we will factor in speed, range and distance
    }

    public float estimateMatch(int ship, int target, IntBag allies, IntBag enemies){
        if(!mData.has(ship) || !mData.has(target))
            throw new IllegalArgumentException("Both Ship and Target must have AIData components");

        float alliesThreat = 0;
        float enemiesThreat = 0;

        for(int i = 0; i < allies.size(); i++){
            alliesThreat += threatCalc(allies.get(i));
        }

        for(int j = 0; j < enemies.size(); j++){
            enemiesThreat += threatCalc(enemies.get(j));
        }

        AIData shipData = mData.get(ship);
        AIData targetData = mData.get(target);

        return 0;
    }
}
