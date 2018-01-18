package aurumvorax.arcturus.artemis.systems.ai.gunnery;

import aurumvorax.arcturus.artemis.components.*;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.ComponentMapper;
import com.artemis.utils.BitVector;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

public enum Aiming{
    INSTANCE;

    private static Vector2 relativeVelocity = new Vector2();
    private static BitVector onTarget = new BitVector();

    // Injected from GunneryAI
    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Weapons> mWeapons;
    private static ComponentMapper<Turret> mTurret;
    private static ComponentMapper<Beam> mBeam;
    private static ComponentMapper<Cannon> mCannon;
    private static ComponentMapper<CollisionRadius> mRadius;
    private static ComponentMapper<Mounted> mMounted;

    public static BitVector aimPrimary(int owner, int target){
        return aim(target, mWeapons.get(owner).auto);
    }

    private static BitVector aim(int target, IntBag weapons){
        if(!mPhysics.has(target)){
            onTarget.clear();
            return onTarget;
        }
        for(int i = 0; i < weapons.size(); i++){
            int w = weapons.get(i);
            if(mBeam.has(w))
                onTarget.set(w, aimBeam(w, target));
            else if(mCannon.has(w)){
                onTarget.set(w, aimCannon(w, target));
            }
        }
        return onTarget;
    }

    private static boolean aimBeam(int beam, int target){
        Vector2 targetPos = mPhysics.get(target).p;
        float range = mBeam.get(beam).maxRange + mRadius.get(target).radius;
        float dist2 = mMounted.get(beam).position.dst2(targetPos);

        if(dist2 > range * range)     // Out of range, don't bother aiming
            return false;
        mTurret.get(beam).target.set(targetPos);
        return(mTurret.get(beam).onTarget);
    }

    private static boolean aimCannon(int cannon, int target){



        return false;
    }
}
