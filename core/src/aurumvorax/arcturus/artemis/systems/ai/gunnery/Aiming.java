package aurumvorax.arcturus.artemis.systems.ai.gunnery;

import aurumvorax.arcturus.artemis.components.Physics2D;
import aurumvorax.arcturus.artemis.components.shipComponents.Weapons;
import com.artemis.ComponentMapper;
import com.artemis.utils.BitVector;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.IntArray;

public class Aiming{

    private static BitVector onTarget = new BitVector();

    private static ComponentMapper<Physics2D> mPhysics;
    private static ComponentMapper<Weapons> mWeapons;


    public static BitVector aimPrimary(int owner, int target){
        return aim(owner, target, mWeapons.get(target).auto);
    }

    private static BitVector aim(int owner, int target, IntBag weapons){
        return onTarget;
    }
}
