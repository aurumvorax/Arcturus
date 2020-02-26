package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Faction;
import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.Projectile;
import aurumvorax.arcturus.artemis.components.Sensors;
import com.artemis.ComponentMapper;

public class SensorsAI{

    private static ComponentMapper<Sensors> mSensors;
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Faction> mFaction;
    private static ComponentMapper<Projectile> mProjectile;


    public void process(int ship){/*
        Sensors s = mSensors.get(ship);
        Faction f = mFaction.get(ship);

        s.reset();

        Vector2 position = mPhysics.get(ship).p;

        for(int i = 0; i < s.ships.size(); i++){
            int target = s.ships.get(i);

            if(position.dst2(mPhysics.get(target).p) > s.range * s.range)
                continue;

            if(mFaction.get(target).faction.equals(f.faction))
                s.friendlyShips.add(target);
            else{
                s.hostileShips.add(target);
            }
        }

        if(s.scanForMissiles){
            for(int i = 0; i < s.missiles.size(); i++){
                int missile = s.missiles.get(i);
                if(!f.faction.equals(mFaction.get(mProjectile.get(missile).firedFrom).faction))
                    s.hostileMissiles.add(missile);
            }
        }*/
    }
}
