package aurumvorax.arcturus.artemis.systems.ai;

import aurumvorax.arcturus.artemis.components.Beam;
import aurumvorax.arcturus.artemis.components.Cannon;
import aurumvorax.arcturus.artemis.components.Launcher;
import aurumvorax.arcturus.artemis.components.Weapons;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class ThreatAssessor{

    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<Launcher> mLauncher;


    public float assess(int ship){
        float threat = 0;
        IntBag w = mWeapons.get(ship).all;

        for(int i = 0; i < w.size(); i++){
            threat += getDPS(w.get(i));
        }

        return threat;
    }

    private static float getDPS(int weapon){

        //TODO - ammo
        if(mBeam.has(weapon))
            return mBeam.get(weapon).dps;

        if(mCannon.has(weapon))
            return mCannon.get(weapon).dps;

        if(mLauncher.has(weapon))
            return mLauncher.get(weapon).dps;

        throw new IllegalArgumentException("Not a valid weapon - " + weapon);
    }
}
