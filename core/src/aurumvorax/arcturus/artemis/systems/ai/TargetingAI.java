package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.Utils;
import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;


public class TargetingAI{

    private static ComponentMapper<Player> mPlayer;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Ship> mShip;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<CollisionRadius> mRadius;


    public void process(int ship){
        Sensors sensors = mSensors.get(ship);
        Weapons weapons = mWeapons.get(ship);
        Vector2 position = mPhysics.get(ship).p;

        // TODO if not player
        // select main target according to personality/threat analysis


        for(int i = 0; i < weapons.pd.size(); i++){
            int weapon = weapons.pd.get(i);
            int target = mTurret.get(weapon).target;

            if((target == -1) || (mShip.has(target))){
                Turret t = mTurret.get(weapon);
                int bestID = -1;
                float best = Float.MAX_VALUE;

                for(int j = 0; j < sensors.hostileMissiles.size(); j++){
                    int missile = sensors.hostileMissiles.get(j);

                    if(Utils.withinAngle(position.angle(mPhysics.get(missile).p), t.sweepMin, t.sweepMax)){
                        float priority = evaluateMissile(missile, position);
                        if(priority < best){
                            best = priority;
                            bestID = missile;
                        }
                    }
                }

                if(bestID != -1){
                    t.target = bestID;
                    continue;
                }

                for(int j = 0; j < sensors.hostileShips.size(); j++){
                    int hostile = sensors.hostileShips.get(j);

                    if(Utils.withinAngle(position.angle(mPhysics.get(hostile).p), t.sweepMin, t.sweepMax)){
                        float priority = evaluateMissile(hostile, position);
                        if(priority < best){
                            best = priority;
                            bestID = hostile;
                        }
                    }
                }

                if(bestID != -1){
                    t.target = bestID;
                }
            }
        }

        for(int i = 0; i < weapons.auto.size(); i++){
            int weapon = weapons.auto.get(i);
            int target = mTurret.get(ship).target;

            if(target == -1){
                Turret t = mTurret.get(weapon);
                int bestID = -1;
                float best = Float.MAX_VALUE;

                for(int j = 0; j < sensors.hostileShips.size(); j++){
                    int hostile = sensors.hostileShips.get(j);

                    if(Utils.withinAngle(position.angle(mPhysics.get(hostile).p), t.sweepMin, t.sweepMax)){
                        float priority = evaluateMissile(hostile, position);
                        if(priority < best){
                            best = priority;
                            bestID = hostile;
                        }
                    }
                }

                if(bestID != -1){
                    t.target = bestID;
                }
            }
        }
    }

    private float evaluateMissile(int target, Vector2 position){
        return position.dst2(mPhysics.get(target).p);
    }
}
