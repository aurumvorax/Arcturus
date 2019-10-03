package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.*;
import com.artemis.ComponentMapper;

public class SensorsAI{

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Faction> mFaction;
    private static ComponentMapper<Projectile> mProjectile;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;


    public void process(int ship){

        Sensors s = mSensors.get(ship);
        Faction f = mFaction.get(ship);

        s.reset();

        for(int i = 0; i < s.ships.size(); i++){
            int target = s.ships.get(i);

            if(mFaction.get(target).faction.equals(f.faction))
                s.friendlyShips.add(target);
            else{
                s.hostileShips.add(target);
                    Weapons w = mWeapons.get(ship);
                    for(int j = 0; j < w.all.size(); j++){
                        int weapon = w.all.get(j);
                        mTurret.get(weapon).target = target;
                    }
            }
        }

        if(s.scanForMissiles){
            for(int i = 0; i < s.missiles.size(); i++){
                int missile = s.missiles.get(i);
                if(!f.faction.equals(mFaction.get(mProjectile.get(missile).firedFrom).faction))
                    s.hostileMissiles.add(missile);
            }
        }
    }
}
